import jieba.analyse
from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse,FileResponse,Response
from fastapi.middleware.cors import CORSMiddleware
import pandas as pd
from rank_bm25 import BM25Okapi
import jieba
import pandas as pd
import numpy as np
from sklearn.impute import SimpleImputer
from scipy.stats import skew
import os
os.chdir(r'/home/data-analysis/python/code/app')
app = FastAPI()

# 允许所有来源、所有方法、所有请求头
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.post("/process_file")
async def process_file(file: UploadFile = File(...)):
    # Read file into pandas dataframe
    if file.filename.endswith('.xlsx'):
        df = pd.read_excel(file.file)
    elif file.filename.endswith('.csv'):
        df = pd.read_csv(file.file)
    else:
        return JSONResponse(content={"error": "Unsupported file format"})

    # Process data here...
    processed_data = df.head()
    # 获取表格属性  返回list
    # print(df.columns[0])
    # 调用自动填充函数
    df_filled, method_used = data_clc(df)
    print("使用的填充方法为：", method_used)
    print("处理后的数据框为：\n", df_filled)
    output = pd.ExcelWriter('data.xlsx')  # 创建一个excel文件
    df_filled.to_excel(output)  # 将数据写入excel文件
    output.close()  # 关闭文件流

    return JSONResponse(content={"message":"处理完成！","code":200})  # 返回文件内容


@app.get("/download_file")
async def download_file(file_name: str):

    return FileResponse(file_name)


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}

@app.get("/model/{question}")
async def model(question: str):
    data = pd.read_excel('question.xlsx')
    # 计算BM25相似度
    # 待比较的两个句子
    result = bm25(question)


    return {"message": result}


def bm25(text):
    score = []
    data = pd.read_excel('question.xlsx')
    # 计算BM25相似度
    # 待比较的两个句子
    s1 = text
    for i in data['content']:
        s2 = i
        # 对两个句子进行分词
        s1_cut = jieba.lcut(s1)
        s2_cut = jieba.lcut(s2)

        # 将两个句子的分词结果放入BM25模型中
        bm25_model = BM25Okapi([s1_cut, s2_cut])

        # 计算两个句子之间的相似度
        similarity = bm25_model.get_scores(s2_cut)[0]
        # similarity取绝对值
        similarity = abs(similarity)
        score.append(similarity)
    # 求出score中的最大值以及它的索引
    index = (score.index(max(score)))
    return data.iloc[index]

def data_clc(df, method=None):
    """
    自动检测缺失值并填充
    :param df: 待处理的数据框
    :param method: 填充缺失值的方法，包括：mean、median、most_frequent、constant。默认为 None（自动选择）。
    :return: 处理后的数据框和使用的方法
    """
    # 检测缺失值
    missing = df.isnull().sum()
    if missing.sum() == 0:
        print("数据框中没有缺失值。")
        return df, "No Imputation"
    else:
        print("数据框中存在缺失值，正在处理中...")
        print("缺失值数量如下：\n", missing)
        # 检查数据集中是否存在字符串列
    # 自动选择填充方法
    if method is None:
        print("正在自动选择填充方法...")
        numeric_cols = df.select_dtypes(include=np.number).columns
        skewed_cols = df[numeric_cols].apply(lambda x: skew(x.dropna()))
        skewed_cols = skewed_cols[skewed_cols > 0.5].index.tolist()
        has_str_col = any(df.dtypes == object)
        if has_str_col:
            imputer = SimpleImputer(strategy='most_frequent')
            method = 'most_frequent'
        else:
            if len(skewed_cols) > 0:
                method = "median"
            else:
                method = "mean"
            print("自动选择的填充方法为：", method)
            # 填充缺失值
            imputer = SimpleImputer(strategy=method)

    df_filled = pd.DataFrame(imputer.fit_transform(df), columns=df.columns)
    # 返回处理后的数据框和使用的方法
    return df_filled, method
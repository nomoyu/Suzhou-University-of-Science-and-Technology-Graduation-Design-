# -*- codeing = utf-8 -*-
# @Time : 2023/3/15 13:38
# @Author :tian
# @File : data_crash.py
# @Software: PyCharm
import pandas as pd
import numpy as np
from sklearn.impute import SimpleImputer
from scipy.stats import skew


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

data = pd.read_excel("F:\SpringMCM2023\data\data.xlsx")
df_filled, method_used = data_clc(data)
print(method_used)
df_filled.to_excel("F:\SpringMCM2023\data\data_fill.xlsx")


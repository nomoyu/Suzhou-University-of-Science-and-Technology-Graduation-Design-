# -*- codeing = utf-8 -*-
# @Time : 2023/4/16 1:20
# @Author :tian
# @File : bm25.py
# @Software: PyCharm
import pandas as pd
import jieba
from rank_bm25 import BM25Okapi


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
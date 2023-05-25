import pandas as pd
import numpy as np
from sklearn.decomposition import PCA
from openpyxl import Workbook
from sklearn.preprocessing import StandardScaler

# 读取数据
df = pd.read_csv('data.csv', header=None, dtype=None,encoding='gbk')

# 将字符串类型的数据转换为数值类型
df = df.apply(pd.to_numeric, errors='coerce')

# 删除包含NaN值的行
df = df.dropna()
# 获取数值类型的列，并进行标准化
num_cols = df.select_dtypes(include='number').columns.tolist()
# # 取出数值类型
# print(num_cols)
# print(df[1])
# scaler = StandardScaler()
# print(df[num_cols])
# data = scaler.fit_transform(df[num_cols])
# # 标准化数据
data = df[num_cols].values
mean = np.mean(data, axis=0)
std = np.std(data, axis=0)
data = (data - mean) / std

# 使用PCA降维
pca = PCA(n_components='mle')
result = pca.fit_transform(data)

# 将结果转换为DataFrame
result_df = pd.DataFrame(result)

non_num_cols = df.select_dtypes(exclude='number').columns.tolist()
# 合并数值类型的列和非数值类型的列，并保存为新的 Excel 文件

new_df = pd.concat([result_df, df[non_num_cols]], axis=1)
new_df.to_excel('new_data.xlsx', index=False)
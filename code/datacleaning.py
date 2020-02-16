import pandas as pd
import numpy as np
pd.set_option('display.max_columns', 10)

############## Import data ################
raw_Data = pd.read_csv (r'C:\Users\Qinyang\Desktop\Part 1.csv', low_memory=False)
dirty_data = pd.DataFrame(raw_Data[1:])
dirty_data['NAICS.id'].astype(str)
data = dirty_data

############## Data cleaning ##############
#### Remove duplicates
# print(temp['NAICS.id'].str.strip().str[-1])
lastDigit = data['NAICS.id'].str.strip().str[-1]
# print(lastDigit)
index = []
for i in range(1, len(lastDigit)):
    if (lastDigit[i] == '0'):
        # print("hi")
        index.append(i)
data.drop(index, inplace = True)
# print(result)
# print(data[1:20]['GEO.id'])
# print(type(data))
data.to_csv(r'C:\Users\Qinyang\Desktop\part11.csv')

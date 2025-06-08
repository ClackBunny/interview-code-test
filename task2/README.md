# extract-country-cities

这是个简单的脚本, 用来统计csv文件中, 某个国家的各个城市出现的次数

## 参数

| 参数           | 默认值               | 说明                                                         |
| -------------- | -------------------- | ------------------------------------------------------------ |
| -c COUNTRY     | CN                   | 国家代码, 例如中国 CN, 荷兰 NL                               |
| -f FILE        | geoloc-Microsoft.csv | 需要统计的文件                                               |
| -s true\|fasle | false                | 是否跳过文件投                                               |
| -o FILE        |                      | 是否把结果输出到文件中, 如果不写就只在终端输出, 写了就把结果写入文件 |

## 示例

统计 `geoloc-Microsoft.csv` 文件中的中国的城市出现的数量

```shell
./extract-country-cities.sh
```

统计 `geoloc.csv` 文件中的荷兰的城市出现的数量, 并输出到结果文件`output.txt`

```shell
./extract-country-cities.sh -f ./geoloc.csv -c NL -o ./output.txt
```


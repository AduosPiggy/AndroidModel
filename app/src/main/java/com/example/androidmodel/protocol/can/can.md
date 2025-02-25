# can protocol

重点看02标准数据帧的那一张详细图片

### 0 参考作者

初步理解 参考作者:B站 爱上半导体  https://www.bilibili.com/video/BV14k4y187e6/?share_source=copy_web&vd_source=805e60303fe593322fa2389786e83a80

案例分析 参考作者: [还没想好~](https://blog.csdn.net/LiuXF93)  https://blog.csdn.net/LiuXF93/article/details/113729294

详细分析 参考作者:[mustfeng](https://blog.csdn.net/u010037269) https://blog.csdn.net/fengge2018/article/details/107592487?spm=1001.2014.3001.5506

### 1 can通信接线方式

#### 1.1 can:  

 可以连接所有设备,双绞线差分信号方式,一次只能一个设备发消息.

![img01](D:\ProJects\Android\AndroidLearn\AndroidModel\AndroidModel\app\src\main\java\com\example\androidmodel\protocol\can\assets\img01.png)

#### 1.2 ecu:

![img02](D:\ProJects\Android\AndroidLearn\AndroidModel\AndroidModel\app\src\main\java\com\example\androidmodel\protocol\can\assets\img02.png)

### 2 标准数据帧



#### 2.1 概览:

![img03](D:\ProJects\Android\AndroidLearn\AndroidModel\AndroidModel\app\src\main\java\com\example\androidmodel\protocol\can\assets\img03.png)

#### 2.2 标准数据帧

![img04](D:\ProJects\Android\AndroidLearn\AndroidModel\AndroidModel\app\src\main\java\com\example\androidmodel\protocol\can\assets\img04.png)

#### 2.3 标准数据帧 补充

##### 2.3.1 IDE决定识别码

​	标准格式,IDE逻辑0,则识别码11位

​	拓展格式,IDE逻辑1,则识别码29位

![img05](D:\ProJects\Android\AndroidLearn\AndroidModel\AndroidModel\app\src\main\java\com\example\androidmodel\protocol\can\assets\img05.png)

##### 2.3.2 控制码中DLC决定数据码

​		DLC的4位二进制决定数据码的长度

​		此处DLC为1000, 即8,证明数据码位8个字节, 1byte=64bit,即数据码长度为64位

![img06](D:\ProJects\Android\AndroidLearn\AndroidModel\AndroidModel\app\src\main\java\com\example\androidmodel\protocol\can\assets\img06.png)

#### 2.4 标准数据帧详细解释:

起始位 都是0; 

识别码 用于识别ECU; 

RTR 区分数据帧0或远程请求帧1;

6位控制码---控制数据长度: 

​		IDE---区分标准格式和拓展格式

​					标准格式:11位识别码 其IDE 为逻辑0

​					拓展格式:29位识别码 其IDE为逻辑1

​		空闲位:即预留位,是 逻辑0

​		DLC(data link control): 数据长度代码

​				如果DLC为1,则后面的数据就一个字节 8位;如果为8,则有64位

数据码: 由控制码的DLC决定长度

CRC: 循环冗余校验位  确保数据的准确性 15位CRC校验码

CRC界定符:是逻辑1. 把后面的信息隔离开

ACK码(2位): 

​	第一位:ACK确认槽-----发送端发送逻辑1,接收端回复逻辑0应答

​	第二位:ACK界定位------是逻辑1

结束位(7位):表示数据帧传输结束

### 3 实际案例分析

​		案例分析 参考作者: [还没想好~](https://blog.csdn.net/LiuXF93)  https://blog.csdn.net/LiuXF93/article/details/113729294
# Federated_search_
包含了许多内容的代码！完成了k-means分类，p1,p2等
K-means：包含了对数据集用K-means分类的代码，其中：
  K_Means包：
  |  K_Means.java:为分类的入口程序
  |  Classify:是根据质心，将整个数据集中的所有文档划分到所属类别中
  |  Collection_initial:集合初始化的过程，（将保存的数据集信息装载到程序中）
  |  AllPath:参数设置！！！很重要，由于代码复用，所以必须要在这个类中，改变一些参数的设定，包括路径，程序的模式等等。
  Structure包：数据结构包！
  |  包含了：基类Base,以及具体的实现类Centroid（质心类），SingleDoc（单个文档类），和初始化这些数据的类
  |  Create_Doc2.java:利用terrier源码的索引内容，初始化所有质心和单个文档！
  |  Relation：重要！将单个文档放入到其所属类别中的类！保证了多线程的一部分
  combineCol包：合并数据集的包
  |  由于数据量巨大，将数据集分开分类，最后通过这个包来合并数据集。（暂时弃用）
  default包：
  |  ChangeName
  |  CheckSchema:检验并发操作最后，数据集中的文档是否有丢失，检查数据集的完整性
  |  ChooseCentroid
  |  Combine_Type
  |  IndexTest
  |  TongJi
  |  test

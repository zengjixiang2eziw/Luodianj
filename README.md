# Patronus

>  为隐私数据提供安全健壮的分布式分析平台

### 1. 背景需求

随着信息爆炸和互联网技术的发展，数据的积累呈几何递增，如何从海量数据中提取有价值的知识已经成为各行业迫切要解决的问题。大量的获取数据是目前数据挖掘，机器学习，计算机视觉，自然语言处理等领域的公司以及研究机构最急迫的需求之一。

而数据隐私安全，则是一个可能大家还没有太过注意，但是却极为重要的一点。前有Facebook数据泄露门，后有Baidu的Robin诚言国人愿意为了方便出卖隐私。这种种都说明了隐私数据安全的意义，但同样，机构和公司的研究，对于业务服务的优化，又是需要以数据来驱动的。

而我们的项目所希望解决，正是与此密切相关的一个问题。

当下据调查，一方面对于巨头公司，其存有大量的有价值的数据，但是因为涉及原始数据隐私和法律法规的关系，其不便于直接对公众开放。另一方面，对于小型机构，个人研究者，也存在着因为获取数据困难，而导致很难进行实验和分析的问题。

如何平衡这两者的需求，正是我们希望解决的问题，因此我们提出了Patronus这样一个构思，通过建立一个数据分享的平台，将每一方的数据离线存储，通过平台记录，中心化追责等措施，尽可能地保护敏感数据，在不精确访问原始真实数据的条件下，得到准确的模型和分析结果，完成安全健壮的隐私数据分析，

更详细的讲，我们期望解决的是，在分布式环境中各方如何在保证每个站点的原始真实数据不被其他站点获取的同时，又能通过各站点共享的数据去分析，学习出有价值的知识。 

在这里我们假设，各行业在知识的获取上存在着竞争，在得到自己所需要的知识的同时，也希望对手尽量少地挖掘出自己想隐藏的信息或知识。 因此分布式隐 私保护数据挖掘在实际中应用得更为广泛。 

例如，不同航空公司希望从共享数据库中分析出优质客户的行为模式，各国政府、公安等系统合作挖掘反恐信息等。 

### 2. 系统设计

下面主要围绕几个方面进行详细阐述。

#### 2.1 隐私数据本地隔离

我们的平台通过群组`Group`的方式管理数据，在建立群组`Group`的时候，即定义数据格式规范，以及其余信息。当用户自己持有数据，选择加入群组`Group`时，本地软件先检测用户是否已经倒入数据，若没有，通知用户先导入规定格式的数据，并根据要加入用户的数据属性和用户信誉读，进行审核，若审核通过，则该用户加入群组，中心对该群组进行更新。

群组是同一类数据的集合，因此我们的学习是以群组为对象的。任何一个在群组中的结点都可以通过花费自己的点数来发起学习任务，任何一个提供数据的结点都可以通过共享数据来获得相应的奖励。

提供的数据并不会被上传到服务器上，当某结点发起任务时，我们将该节点的任务请求，所要运行的代码发送到其余结点上，代码在其余结点上离线运行，并且返回分析的模型文件，或是数据分析图标，或是神经网络的模型图等。

#### 2.2 代码检测及可追责

首先对于代码检测，我们定义在服务器端检测代码的安全性，我们采取如代码的恶意行为检测技术即包括静态类型检查，动态检查，动态污点分析等方式进行代码分析，分析的结果作为下一步是否分发代码的依据，防止用户通过运行恶意代码，损害或者是窃取他人的数据。

其次对于代码可追责的部分，设计初衷源于，我们可能并未能成功检测到代码中的恶意行为，但是我们仍然需要通过平台将任务的发起者和其代码绑定。

具体实施措施为，用户接受代码后，软件界面出现提示，用户可以在软件内部浏览代码及其规格说明。若用户同意，则将该代码加入本地任务队列。

在软件层面，我们预定义若干种恶意行为，（计划在未来考虑云安全的方式）。若在计算过程中发现恶意行为，用户上报，中心立即广播群组用户停止计算该任务，并在公示中突出显示“恶意”代码。经过平台方的审查，若确认改代码为恶意，冻结恶意账号，并将该账号的所有代码写上“恶意标签”，防止他人下载自己使用。

计算完成后，软件启动恶意回传行为的模式检测，通过检测其回传的模型格式以及具体模型内容，若发现隐私数据特征值，提取后显示给用户并上报中心，经本地用户和中心互相确认为隐私数据回传行为，等同于恶意代码处理。

#### 2.3 水印防抄袭

我们为每一个用户的代码及其自身的ID打上水印，在中心平台记录，用户可以选择自己的代码是否公开以及是否可以被使用。我们通过水印的方式保障在平台上能够运行的用户代码是符合代码撰写人的权限要求的。

其具体实现方式为，用户分发任务时，首先导入自己写入的代码及代码对应的规格说明，然后使用私钥加密后上传服务器，服务器通过用户的公钥解密，首先将该代码与现存的水印库进行比对，若发现该代码已经在水印库中存在，说明该发起者盗用他人代码，拒绝申请并降低其信誉度，同时通过之前的认证方式通知被抄袭者。若为本人原创新代码，使用UID作为种子对用户的代码做水印处理，将处理后的代码存放在中心公示，并向群组内的用户分发水印处理后的代码（若为以前上传过的代码，直接分发缓存）。

#### 2.4 分布式共享学习平台

对于我们的平台，一次任务的过程同时也是一次分布式学习的过程，我们假设数据的提供方用于维护数据的服务器都拥有足够算力，因此对于个人数据分析者，也可以通过付费的方式，无需自身提供数据集，只需要构建相应的数据分析代码，我们便将其分发到分布式的数据环境中，最终返回分析或训练的结果，通过这样安全的隐私数据『开放』，也能够促进小型机构及个人研究者的研究分析效率，从而为相关领域的研究带来更多的发展机会。

### 3. 目前进展

前期完成了项目的设计：

- 数据流图的设计规划：https://www.processon.com/view/link/5af403f2e4b077366ec58e6a
- 项目的UI界面设计文档：https://www.processon.com/view/link/5b01030ce4b0ceccca8954cb
- 后端服务器的设计文档：https://www.processon.com/view/link/5b01032fe4b06a40445e79fd

中期截止目前完成了原型`Prototype`版本的开发。

项目前端及底层见：https://github.com/Luodian/HCCP-Patronus

项目的服务器后端见：https://github.com/PracNeyman/PatServer

### 4. 后续规划

目前项目只是一个原型，主要核心为隐私数据的分布式学习，但是后续仍然有很多可以加入安全要素的地方，主要有以下的几个方向的思路。

- 在代码可追责部分，因为本身其实就是分布式平台，可以尝试抛开中心化服务器记录代码->用户的关系，从而将每一次的`用户发起代码运算`这个行为记录在区块中，形成区块链，机制可以采用POS的方式，平台提供Token，Token可以用作打包区块时的消耗，从而换取积分，积分可以用于发起一次计算任务。

- 在各层传输的过程中，增加更多的安全要素，包括代码的加解密，**任务发放过程中服务器和客户机做消息认证**，公私密钥的交换等等方向。

- 考虑增加更多的隐私数据保护的手段，根据现有的学术研究资料指出[1]，我们可以考虑如**数据扰动技术、 数据加密和查询限制**等。

  -  数据扰动技术是采用数据交换、添加噪声等方法扰动原始数据，使敏感数据失真但能保证通过数 据挖掘工具挖掘出的知识真实有效，如随机扰动、随机应答、阻塞和凝聚等。

    扰动技术隐藏了真实数据，挖掘者只能在被扰动的数据上挖掘知识。 扰动后的数据依旧保持某些属性不变，因此在扰动后的数据上挖掘得到的知识是正确有效的。 在 DPPDM 中最常见的扰动方法就是为原始数据添加噪声。 

  - 加密技术是对原始数据加密以保护隐私，如安全多方技术。SMC技术是解决两个或多个互不信任的参与方之间保护隐私的协同计算问题，SMC 要确保输入的独立性、计算的正确性，同时又要保护各输入信息不泄露给参与计算的其他成员。

    其中SMC技术的观点和我们的平台类似，但是我们可以参考如**垂直（水平）多方数据存储技术**，来完善我们的平台。

  - 查询限制是通过限制数据的查询，避免数据挖掘者获取完整原始数据的方法，以实现隐私保护，如通过抑制、泛化、数据抽样、数据划分等原则匿名化数据。 

- 代码检测：云安全，人工判别代码，重复构建新的分类器。

- 沙箱机制：将代码放入沙箱，通过平台的接口调用数据集。从而保证客户机系统安全以及数据安全。

### 5. 参考

1. 刘英华, 杨炳儒, 马楠, 等. 分布式隐私保护数据挖掘研究倡[J]. 计算机应用研究, 2011, 28(10).


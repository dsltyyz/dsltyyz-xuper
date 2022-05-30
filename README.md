# 百度超级链-baidu xuperchain
**官方文档 [传送门](https://xuper.baidu.com/n/xuperdoc/index.html)** 
## 1.环境安装
### 1.1 系统及语言
~~~
操作系统 centos7
开发语言 java
合约语言 java
~~~
### 1.2 java环境
~~~
#1.安装
yum -y install java-1.8.0-openjdk java-1.8.0-openjdk-devel
#2.查看版本
java -version
~~~
### 1.3 go语言编译环境 
**go1.18.2.linux-amd64.tar.gz [传送门](https://golang.google.cn/dl)**
~~~
#1.解压
tar -C /usr/local -zxf go1.18.2.linux-amd64.tar.gz
#2.创建文件夹gopath
mkdir /home/gopath
#3.配置环境变量
vi /etc/profile
末尾追加
#golang env config
export GO111MODULE=on
export GOROOT=/usr/local/go 
export GOPATH=/home/gopath
export PATH=$PATH:$GOROOT/bin:$GOPATH/bin
#4.刷新配置
source /etc/profile
#5.查看go版本
go version
#go version go1.18.2 linux/amd64
#6.配置代理
go env -w GOPROXY=https://goproxy.cn,direct
go env
~~~
### 1.4 c++语言编译环境
~~~
#1.安装
yum -y install gcc gcc-c++ kernel-devel
#2.查看版本
gcc -v
#gcc version 4.8.5 20150623 (Red Hat 4.8.5-44) (GCC)
~~~
### 1.5 xuperchain环境
**xuperchain-release-v5.2.zip [传送门](https://github.com/xuperchain/xuperchain/tree/master)**
~~~
#1.解压
unzip xuperchain-release-v5.2.zip
#2.修改文件夹名称
mv xuperchain-release-v5.2 xuperchain
#3.进入目录并编译
cd xuperchain
#4.启动
#4.1单机版
#Go版本1.11 or later先调用make test安装依赖
make test
make
cd output
sh control.sh start
./bin/xchain-cli status
#4.2多节点
make
make testnet
cd ./testnet/node1
sh ./control.sh start
cd ../node2
sh ./control.sh start
cd ../node3
sh ./control.sh start
./bin/xchain-cli status -H :37101
./bin/xchain-cli status -H :37102
./bin/xchain-cli status -H :37103
~~~
## 2.xuper配置
### 2.1 创建新账号
~~~
bin/xchain-cli account newkeys --output data/bob
~~~
### 2.2配置加密-国密算法
~~~
bin/xchain-cli account newkeys --output data/alice --crypto gm
~~~
### 2.3创建合约账号-16位数字
~~~
bin/xchain-cli account new --account 1111111111111111 --fee 2000
#address: TeyyPLpp9L7QAcxHangtcHTu7HUZ6iydY
#account name: XC1111111111111111@xuper
~~~
## 3.xuper资源
### 3.1 查询资源余额
~~~
#查询普通账户
bin/xchain-cli account balance --keys data/bob -H 127.0.0.1:37101
#根据地址查询该账户余额
bin/xchain-cli account balance TeyyPLpp9L7QAcxHangtcHTu7HUZ6iydY -H 127.0.0.1:37101
#查询合约账户
bin/xchain-cli account balance XC1111111111111111@xuper -H 127.0.0.1:37101
~~~
### 3.2 转账
~~~
#合约账户余额-发布及调用合约都需要
bin/xchain-cli transfer --to XC1111111111111111@xuper --amount 100000000 --keys data/keys/ -H 127.0.0.1:37101
#tx: 5bad209b52264a60ede1ae7a51db184a4db7874707f7c1e65ad561033494c1bc
~~~
### 3.3 查询交易信息
~~~
bin/xchain-cli tx query 5bad209b52264a60ede1ae7a51db184a4db7874707f7c1e65ad561033494c1bc -H 127.0.0.1:37101
#blockid: 0c19b72f12611ad37314bae8562be217f00cdae8090bcf8b8a9bf317c4354c2c
~~~
### 3.4 查询block信息
~~~
bin/xchain-cli block 0c19b72f12611ad37314bae8562be217f00cdae8090bcf8b8a9bf317c4354c2c -H 127.0.0.1:37101
~~~
## 4.JAVA合约
### 4.1 pom.xml
~~~
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.dsltyyz.xuper</groupId>
    <artifactId>dsltyyz-game-assets</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.baidu.xuper</groupId>
            <artifactId>java-contract-sdk</artifactId>
            <version>0.2.0</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.68</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>

    <build>
        <pluginManagement>
            <!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
            <plugins>
                <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->
                <plugin>
                    <artifactId>maven-clean-plugin</artifactId>
                    <version>3.1.0</version>
                </plugin>
                <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->
                <plugin>
                    <artifactId>maven-resources-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.8.0</version>
                </plugin>
                <plugin>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>2.22.1</version>
                </plugin>
                <plugin>
                    <artifactId>maven-jar-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-install-plugin</artifactId>
                    <version>2.5.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-deploy-plugin</artifactId>
                    <version>2.8.2</version>
                </plugin>
                <!-- site lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#site_Lifecycle -->
                <plugin>
                    <artifactId>maven-site-plugin</artifactId>
                    <version>3.7.1</version>
                </plugin>
                <plugin>
                    <artifactId>maven-project-info-reports-plugin</artifactId>
                    <version>3.0.0</version>
                </plugin>
            </plugins>
        </pluginManagement>

        <plugins>
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.dsltyyz.xuper.contract.GameAssetsContract</mainClass>
                        </manifest>
                    </archive>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id> <!-- this is used for inheritance merges -->
                        <phase>package</phase> <!-- bind to the packaging phase -->
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
~~~
### 4.2 合约代码
~~~
public class GameAssetsContract implements Contract {

    private final String ADMIN = "admin";
    private final String INITIATOR = "initiator";
    private final String TYPE_ID = "typeId";
    private final String TYPE_JSON_DATA = "typeJsonData";
    private final String USER_ID = "userId";
    private final String ASSET_ID = "assetId";
    private final String USER_ASSET_PREFIX = "user_asset_";
    private final String ASSET_TYPE_PREFIX = "asset_type_";
    private final String ASSET_TO_USER_PREFIX = "asset_to_user_";

    @ContractMethod
    @Override
    public Response initialize(Context context) {
        byte[] admin = context.args().get(ADMIN);
        if (admin == null) {
            return Response.error("missing admin address");
        }
        context.putObject(ADMIN.getBytes(), admin);
        return Response.ok("initialize success".getBytes());
    }

    private boolean checkAdmin(Context context, byte[] caller) {
        byte[] admin = context.getObject(ADMIN.getBytes());
        if (admin == null) {
            return false;
        }
        return Arrays.equals(admin, caller);
    }

    @ContractMethod
    public Response addAssetType(Context context) {
        byte[] initiator = context.args().get(INITIATOR);
        if (initiator == null) {
            return Response.error("missing initiator");
        }
        if (!checkAdmin(context, initiator)) {
            return Response.error("only the admin can add new asset type");
        }
        byte[] typeId = context.args().get(TYPE_ID);
        if (typeId == null) {
            return Response.error("missing 'typeId' as asset type identity");
        }
        byte[] typeJsonData = context.args().get(TYPE_JSON_DATA);
        if (typeJsonData == null) {
            return Response.error("missing 'typeJsonData' as asset type data");
        }
        String typekey = ASSET_TYPE_PREFIX + new String(typeId);
        if (context.getObject(typekey.getBytes()) != null) {
            return Response.error("the typeId is already exist, please check again");
        }
        context.putObject(typekey.getBytes(), typeJsonData);
        return Response.ok(typeId);
    }

    @ContractMethod
    public Response getAssetTypeList(Context context) {
        Iterator<ContractIteratorItem> contractIteratorItemIterator = context.newIterator(ASSET_TYPE_PREFIX.getBytes(), (ASSET_TYPE_PREFIX + "~").getBytes());
        List<Map<String, String>> list = new ArrayList<>();
        while (contractIteratorItemIterator.hasNext()) {
            ContractIteratorItem next = contractIteratorItemIterator.next();
            Map<String, String> map = new TreeMap<>();
            map.put(TYPE_ID, new String(next.getKey()).substring(ASSET_TYPE_PREFIX.length()));
            map.put(TYPE_JSON_DATA, new String(next.getValue()));
            list.add(map);
        }
        return Response.ok(JSONArray.toJSONString(list).getBytes());
    }

    @ContractMethod
    public Response assignNewAssetToUser(Context context) {
        byte[] initiator = context.args().get(INITIATOR);
        if (initiator == null) {
            return Response.error("missing initiator");
        }
        if (!checkAdmin(context, initiator)) {
            return Response.error("only the admin can add new asset type");
        }
        byte[] userId = context.args().get(USER_ID);
        if (userId == null) {
            return Response.error("missing userId");
        }
        byte[] typeId = context.args().get(TYPE_ID);
        if (typeId == null) {
            return Response.error("missing typeId");
        }
        byte[] assetId = context.args().get(ASSET_ID);
        if (assetId == null) {
            return Response.error("missing assetId");
        }
        String assetKey = ASSET_TO_USER_PREFIX + new String(assetId);
        if (context.getObject(assetKey.getBytes()) != null) {
            return Response.error("he assetId is already exist, please check again");
        }
        String userAssetKey = USER_ASSET_PREFIX + new String(userId) + "_" + new String(assetId);
        context.putObject(userAssetKey.getBytes(), typeId);
        context.putObject(assetKey.getBytes(), userId);
        return Response.ok(assetId);
    }

    @ContractMethod
    public Response getAssetsByUser(Context context) {
        byte[] initiator = context.args().get(INITIATOR);
        if (initiator == null) {
            return Response.error("missing initiator");
        }
        byte[] userId = initiator;
        if (checkAdmin(context, initiator)) {
            // admin can get the asset data of other users
            userId = context.args().get(USER_ID);
        }
        List<Map<String, String>> list = new ArrayList<>();
        String userAssetKey = USER_ASSET_PREFIX + new String(userId) + "_";
        Iterator<ContractIteratorItem> contractIteratorItemIterator = context.newIterator(userAssetKey.getBytes(), (userAssetKey + "~").getBytes());
        while (contractIteratorItemIterator.hasNext()) {
            ContractIteratorItem next = contractIteratorItemIterator.next();
            Map<String, String> map = new TreeMap<>();
            String assetTypeKey = ASSET_TYPE_PREFIX + new String(next.getValue());
            byte[] typeJsonData = context.getObject(assetTypeKey.getBytes());
            if (typeJsonData == null) {
                continue;
            }
            map.put(ASSET_ID, new String(next.getKey()).substring((ASSET_TYPE_PREFIX + new String(userId) + "_").length()));
            map.put(TYPE_ID, new String(next.getValue()));
            map.put(TYPE_JSON_DATA, new String(typeJsonData));
            list.add(map);
        }
        return Response.ok(JSONArray.toJSONString(list).getBytes());
    }

    @ContractMethod
    public Response tradeAsset(Context context) {
        byte[] initiator = context.args().get(INITIATOR);
        if (initiator == null) {
            return Response.error("missing initiator");
        }
        byte[] userId = context.args().get(USER_ID);
        ;
        if (userId == null) {
            return Response.error("missing userId");
        }
        byte[] assetId = context.args().get(ASSET_ID);
        if (assetId == null) {
            return Response.error("missing assetId");
        }
        String initiatorAssetKey = USER_ASSET_PREFIX + new String(initiator) + "_" + new String(assetId);
        String userAssetKey = USER_ASSET_PREFIX + new String(userId) + "_" + new String(assetId);
        byte[] typeId = context.getObject(initiatorAssetKey.getBytes());
        if (typeId == null) {
            return Response.error("you don't have assetId:" + assetId);
        }
        context.deleteObject(initiatorAssetKey.getBytes());
        context.putObject(userAssetKey.getBytes(), typeId);
        return Response.ok(assetId);
    }

    public static void main(String[] args) {
        Driver.serve(new GameAssetsContract());
    }
}
~~~
### 4.3打包
~~~
mvn clean package -DskipTests
#生成如下2个jar 选用带with-dependencies的jar
#dsltyyz-game-assets-1.0.0.jar
#dsltyyz-game-assets-1.0.0-jar-with-dependencies.jar
~~~
## 5.发布及升级
建议将jar放在xuperchain/output目录下 简化指令
### 5.1 发布
~~~
./bin/xchain-cli native deploy --account XC1111111111111111@xuper --fee 15587517 --runtime java --cname javagameassets dsltyyz-game-assets-1.0.0-jar-with-dependencies.jar -a '{"admin":"admin"}'
~~~
### 5.2 升级
~~~
#合约升级与合约部署的命令十分类似，区别在于
#不需要指定 runtime 
#不需要指定初始化参数
./bin/xchain-cli native upgrade --account XC1111111111111111@xuper --fee 15587517 --cname javagameassets dsltyyz-game-assets-1.0.0-jar-with-dependencies.jar
~~~
## 6.JAVA调用合约示例
### 6.1 pom.xml
~~~
<dependencies>
    ...
    <dependency>
        <groupId>com.baidu.xuper</groupId>
        <artifactId>xuper-java-sdk</artifactId>
        <version>0.2.0</version>
    </dependency>
    ...
</dependencies>
~~~
### 6.2 XuperComponent
~~~
@Component
public class XuperComponent {

    @Value("${xuper.client}")
    private String target;

    @Value("${xuper.account-mnemonic}")
    private String mnemonic;

    private XuperClient xuperClient;
    private Account account;

    @PostConstruct
    public void init() {
        xuperClient = new XuperClient(target);
        account = Account.retrieve(mnemonic, 2);
    }

    public XuperClient getClient(){
        return xuperClient;
    }
    public Account getAccount(){
        return account;
    }
}
~~~
### 6.3 GameAssetsController
~~~
@Api(value = "游戏资产controller", tags = {"游戏资产"})
@RestController
@RequestMapping("game/assets")
public class GameAssetsController {

    @Resource
    private XuperComponent xuperComponent;

    @ApiOperation(value = "创建资产类型")
    @PostMapping("type")
    public CommonResponse<String> addAssetType(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k, v) -> {
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.invokeContract(account, "native", "javagameassets", "addAssetType", args);
        System.out.println("invoke txid: " + tx.getTxid());
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(tx.getContractResponse().getBodyStr());
    }

    @ApiOperation(value = "获取资产类型列表")
    @PostMapping("type/list")
    public CommonResponse<List<Map<String, String>>> getAssetTypeList(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k, v) -> {
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.queryContract(account, "native", "javagameassets", "getAssetTypeList", args);
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(JSONObject.parseObject(tx.getContractResponse().getBodyStr(), new TypeReference<List<Map<String, String>>>() {
        }));
    }

    @ApiOperation(value = "系统分配新资产给用户")
    @PostMapping("assignNewAssetToUser")
    public CommonResponse assignNewAssetToUser(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k, v) -> {
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.invokeContract(account, "native", "javagameassets", "assignNewAssetToUser", args);
        System.out.println("invoke txid: " + tx.getTxid());
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(tx.getContractResponse().getBodyStr());
    }

    @ApiOperation(value = "资产交易")
    @PostMapping("tradeAsset")
    public CommonResponse tradeAsset(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k, v) -> {
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.invokeContract(account, "native", "javagameassets", "tradeAsset", args);
        System.out.println("invoke txid: " + tx.getTxid());
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(tx.getContractResponse().getBodyStr());
    }

    @ApiOperation(value = "获取用户资产列表")
    @PostMapping("list")
    public CommonResponse<List<Map<String, String>>> getAssetsByUser(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k, v) -> {
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.queryContract(account, "native", "javagameassets", "getAssetsByUser", args);
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(JSONObject.parseObject(tx.getContractResponse().getBodyStr(), new TypeReference<List<Map<String, String>>>() {
        }));
    }
}
~~~
## 7.基础操作实例
~~~
@Api(value = "超级链controller", tags = {"超级链"})
@RestController
@RequestMapping("xuperchain")
public class XuperchainController {

    @Resource
    private XuperComponent xuperComponent;

    @ApiOperation(value = "创建账户")
    @PostMapping("account")
    public CommonResponse<AccountVO> createAccount() {
        Account account = Account.create(1, 2);
        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(account, vo);
        return new CommonResponse<>(vo);
    }

    @ApiOperation(value = "创建合约账户")
    @PostMapping("contractAccount")
    public CommonResponse<String> createContractAccount(@RequestParam String mnemonic, @RequestParam String contractAccountName) {
        Assert.isTrue(contractAccountName.length() == 16, "账户名为16位");
        Account account = Account.retrieve(mnemonic, 2);
        XuperClient client = xuperComponent.getClient();
        client.createContractAccount(account, contractAccountName);
        return new CommonResponse<>("XC" + contractAccountName + "@xuper");
    }

    @ApiOperation(value = "获取账户余额")
    @GetMapping("account/balance")
    public CommonResponse<BigInteger> getBalance(@RequestParam String contractAccount) {
        XuperClient client = xuperComponent.getClient();
        return new CommonResponse<>(client.getBalance(contractAccount));
    }

    @ApiOperation(value = "获取合约账户余额详情")
    @GetMapping("contractAccount/balanceDetail")
    public CommonResponse<?> getBalanceDetails(@RequestParam String contractAccount) {
        XuperClient client = xuperComponent.getClient();
        return new CommonResponse<>(client.getBalanceDetails(contractAccount));
    }

    @ApiOperation(value = "向合约账户余额转账")
    @PutMapping("contractAccount/balance")
    public CommonResponse<String> transferContractAccountBalance(@RequestParam String mnemonic, @RequestParam String contractAccount, @RequestParam BigInteger amount, @RequestParam String fee) {
        Account account = Account.retrieve(mnemonic, 2);
        XuperClient client = xuperComponent.getClient();
        Transaction transfer = client.transfer(account, contractAccount, amount, fee);
        return new CommonResponse<>(transfer.getTxid());
    }

    /*@ApiOperation(value = "合约账户部署合约")
    @PostMapping("contractAccount/contract")
    public CommonResponse deployContract(@RequestParam String contractAccount) {
        XuperClient client = xuperComponent.getClient();
        //client.deployWasmContract()
        return new CommonResponse();
    }*/

    @ApiOperation(value = "合约账户调用合约")
    @PostMapping("contract/{contract}/{method}")
    public CommonResponse<String> invokeContract(@RequestParam String mnemonic, @PathVariable String contract, @PathVariable String method, @RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k,v)->{
            args.put(k, v.getBytes());
        });
        Account account = Account.retrieve(mnemonic, 2);
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.invokeContract(account, "native", contract, method, args);
        System.out.println("invoke txid: " + tx.getTxid());
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(tx.getContractResponse().getBodyStr());
    }

    @ApiOperation(value = "合约账户查询合约")
    @PutMapping("contract/{contract}/{method}")
    public CommonResponse<String> queryContract(@RequestParam String mnemonic, @PathVariable String contract, @PathVariable String method, @RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k,v)->{
            args.put(k, v.getBytes());
        });
        Account account = Account.retrieve(mnemonic, 2);
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.queryContract(account, "native", contract, method, args);
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(tx.getContractResponse().getBodyStr());
    }

    @ApiOperation(value = "查询事务")
    @GetMapping("tx/{txId}")
    public CommonResponse<String> queryTx(@PathVariable String txId){
        XuperClient client = xuperComponent.getClient();
        XchainOuterClass.Transaction tx = client.queryTx(txId);
        //部分源数据需要进行Hex加密再转字符串
        byte[] encode = Hex.encode(tx.getBlockid().toByteArray());
        return new CommonResponse<>(new String(encode));
    }

    @ApiOperation(value = "查询事务")
    @GetMapping("block/{blockId}")
    public CommonResponse<String> queryBlock(@PathVariable String blockId) {
        XuperClient client = xuperComponent.getClient();
        XchainOuterClass.InternalBlock internalBlock = client.queryBlock(blockId);
        //部分源数据需要进行Hex加密再转字符串
        return new CommonResponse<>(new String(internalBlock.getProposer().toByteArray()));
    }

}
~~~
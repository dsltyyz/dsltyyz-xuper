package com.dsltyyz.xuper.xuperchain.openapi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.xuper.api.Account;
import com.baidu.xuper.api.Transaction;
import com.baidu.xuper.api.XuperClient;
import com.baidu.xuper.pb.XchainOuterClass;
import com.dsltyyz.bundle.common.response.CommonResponse;
import com.dsltyyz.xuper.xuperchain.component.XuperComponent;
import com.dsltyyz.xuper.xuperchain.vo.AccountVO;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 超级链前端控制器
 * </p>
 *
 * @author dsltyyz
 * @since 2022-5-18
 */
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

        return new CommonResponse<>(client.getBalance(contractAccount));
    }

    @ApiOperation(value = "向合约账户余额转账")
    @PutMapping("contractAccount/balance")
    public CommonResponse<String> transferContractAccountBalance(@RequestParam String mnemonic, @RequestParam String contractAccount, @RequestParam BigInteger amount, @RequestParam String fee) {
        Account account = Account.retrieve(mnemonic, 2);
        XuperClient client = xuperComponent.getClient();
        Transaction transfer = client.transfer(account, contractAccount, amount, fee);
        return new CommonResponse<>(transfer.getTxid());
    }

    @ApiOperation(value = "合约账户部署合约")
    @PostMapping("contractAccount/contract")
    public CommonResponse deployContract(@RequestParam String mnemonic, @RequestParam String contractAccount, @RequestParam("contractName") String contractName, @RequestPart("file") MultipartFile file, @RequestParam String param) throws IOException {
        Map<String, byte[]> args = new HashMap<>();
        if (!StringUtils.isEmpty(param)) {
            JSONObject.parseObject(param, new TypeReference<Map<String, String>>() {
            }).forEach((k, v) -> {
                args.put(k, v.getBytes());
            });
        }
        XuperClient client = xuperComponent.getClient();
        //1.请确保合约账户属于当前账户
        //2.请确保账户及合约账户下有足够余额
        Account account = Account.retrieve(mnemonic, 2);
        account.setContractAccount(contractAccount);
        //native支持java及go
        Transaction contract = client.deployNativeContract(account, file.getBytes(), contractName, "java", args);
        System.out.println(contract.getTxid());
        System.out.println(contract.getGasUsed());
        System.out.println(JSONObject.toJSONString(contract.getContractResponse()));
        return new CommonResponse();
    }

    @ApiOperation(value = "合约账户升级部署合约")
    @PutMapping("contractAccount/contract")
    public CommonResponse upgradeContract(@RequestParam String mnemonic, @RequestParam String contractAccount, @RequestParam("contractName") String contractName, @RequestPart("file") MultipartFile file) throws IOException {
        XuperClient client = xuperComponent.getClient();
        //1.请确保合约账户属于当前账户
        //2.请确保账户及合约账户下有足够余额
        Account account = Account.retrieve(mnemonic, 2);
        account.setContractAccount(contractAccount);
        //native支持java及go
        Transaction contract = client.upgradeNativeContract(account, file.getBytes(), contractName);
        System.out.println(contract.getTxid());
        System.out.println(contract.getGasUsed());
        System.out.println(JSONObject.toJSONString(contract.getContractResponse()));
        return new CommonResponse();
    }

    @ApiOperation(value = "合约账户调用合约")
    @PostMapping("contract/{contract}/{method}")
    public CommonResponse<String> invokeContract(@RequestParam String mnemonic, @PathVariable String contract, @PathVariable String method, @RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k, v) -> {
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
        param.forEach((k, v) -> {
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
    public CommonResponse<String> queryTx(@PathVariable String txId) {
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

    @ApiOperation(value = "查询合约数目")
    @GetMapping("contractAmount")
    public CommonResponse<String> queryContractAmount() {
        XuperClient client = xuperComponent.getClient();
        return new CommonResponse<>();
    }


}
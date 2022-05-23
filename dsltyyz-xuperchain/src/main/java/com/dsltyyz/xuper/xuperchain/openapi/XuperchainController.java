package com.dsltyyz.xuper.xuperchain.openapi;

import com.baidu.xuper.api.Account;
import com.baidu.xuper.api.Transaction;
import com.baidu.xuper.api.XuperClient;
import com.dsltyyz.bundle.common.response.CommonResponse;
import com.dsltyyz.xuper.xuperchain.component.XuperComponent;
import com.dsltyyz.xuper.xuperchain.vo.AccountVO;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @ApiOperation(value = "获取合约账户余额")
    @GetMapping("contractAccount/balance")
    public CommonResponse getBalance(@RequestParam String contractAccount) {
        XuperClient client = xuperComponent.getClient();
        return new CommonResponse(client.getBalance(contractAccount));
    }

    @ApiOperation(value = "获取合约账户余额详情")
    @GetMapping("contractAccount/balanceDetail")
    public CommonResponse getBalanceDetails(@RequestParam String contractAccount) {
        XuperClient client = xuperComponent.getClient();
        return new CommonResponse(client.getBalanceDetails(contractAccount));
    }

    @ApiOperation(value = "向合约账户余额转账")
    @PutMapping("contractAccount/balance")
    public CommonResponse transferContractAccountBalance(@RequestParam String mnemonic, @RequestParam String contractAccount, @RequestParam BigInteger amount, @RequestParam String fee) {
        Account account = Account.retrieve(mnemonic, 2);
        XuperClient client = xuperComponent.getClient();
        client.transfer(account, contractAccount, amount, fee);
        return new CommonResponse();
    }

    @ApiOperation(value = "合约账户部署合约")
    @PostMapping("contractAccount/contract")
    public CommonResponse deployContract(@RequestParam String contractAccount) {
        XuperClient client = xuperComponent.getClient();
//        client.deployWasmContract()
        return new CommonResponse();
    }

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

}
package com.dsltyyz.xuper.xuperchain.openapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 游戏资产前端控制器
 * </p>
 *
 * @author dsltyyz
 * @since 2022-5-18
 */
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
        param.forEach((k,v)->{
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
    public CommonResponse<List<Map<String,String>>> getAssetTypeList(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k,v)->{
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.queryContract(account, "native", "javagameassets", "getAssetTypeList", args);
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(JSONObject.parseObject(tx.getContractResponse().getBodyStr(), new TypeReference<List<Map<String,String>>>(){}));
    }

    @ApiOperation(value = "系统分配新资产给用户")
    @PostMapping("assignNewAssetToUser")
    public CommonResponse assignNewAssetToUser(@RequestBody Map<String, String> param){
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k,v)->{
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
    public CommonResponse tradeAsset(@RequestBody Map<String, String> param){
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k,v)->{
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
    public CommonResponse<List<Map<String,String>>> getAssetsByUser(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k,v)->{
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.queryContract(account, "native", "javagameassets", "getAssetsByUser", args);
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(JSONObject.parseObject(tx.getContractResponse().getBodyStr(), new TypeReference<List<Map<String,String>>>(){}));
    }
}
package com.dsltyyz.xuper.xuperchain.openapi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.xuper.api.Account;
import com.baidu.xuper.api.Transaction;
import com.baidu.xuper.api.XuperClient;
import com.dsltyyz.bundle.common.response.CommonResponse;
import com.dsltyyz.bundle.common.util.FileUtils;
import com.dsltyyz.xuper.xuperchain.component.XuperComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 哈希存证前端控制器
 * </p>
 *
 * @author dsltyyz
 * @since 2022-5-18
 */
@Api(value = "哈希存证controller", tags = {"哈希存证"})
@RestController
@RequestMapping("hash/deposit")
public class HashDepositController {

    @Resource
    private XuperComponent xuperComponent;

    @ApiOperation(value = "文件存储")
    @PostMapping("file")
    public CommonResponse<String> storeFile(@RequestParam Long userId, @RequestPart MultipartFile file) throws IOException {
        Map<String, String> fileMap = new HashMap<>();
        fileMap.put("name", file.getOriginalFilename());
        fileMap.put("base64", FileUtils.inputStreamToBase64(file.getInputStream()));
        fileMap.put("hashId", FileUtils.md5HashCode(file.getInputStream()));
        fileMap.put("userId", String.valueOf(userId));
        Map<String, byte[]> args = new HashMap<>();
        args.put("userId", String.valueOf(userId).getBytes());
        args.put("hashId", FileUtils.md5HashCode(file.getInputStream()).getBytes());
        args.put("file", JSONObject.toJSONBytes(fileMap));
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.invokeContract(account, "native", "javahashdeposit", "storeFile", args);
        System.out.println("invoke txid: " + tx.getTxid());
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(tx.getContractResponse().getBodyStr());
    }

    @ApiOperation(value = "获取用户列表")
    @PostMapping("user/list")
    public CommonResponse<Set<String>> queryUserList(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k, v) -> {
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.queryContract(account, "native", "javahashdeposit", "queryUserList", args);
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(JSONObject.parseObject(tx.getContractResponse().getBodyStr(), new TypeReference<Set<String>>() {
        }));
    }

    @ApiOperation(value = "获取用户文件列表")
    @PostMapping("user/file/list")
    public CommonResponse<List<String>> queryFileByUser(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k, v) -> {
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.queryContract(account, "native", "javahashdeposit", "queryFileByUser", args);
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(JSONObject.parseObject(tx.getContractResponse().getBodyStr(), new TypeReference<List<String>>() {
        }));
    }

    @ApiOperation(value = "获取文件信息")
    @PostMapping("/file/info")
    public CommonResponse<JSONObject> queryFileByHash(@RequestBody Map<String, String> param) {
        Map<String, byte[]> args = new HashMap<>();
        param.forEach((k, v) -> {
            args.put(k, v.getBytes());
        });
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.queryContract(account, "native", "javahashdeposit", "queryFileByHash", args);
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        return new CommonResponse<>(JSONObject.parseObject(tx.getContractResponse().getBodyStr()));
    }

    @ApiOperation(value = "获取文件图片")
    @GetMapping("/file/image/{hash}")
    public void queryImageFileByHash(@PathVariable String hash,  HttpServletResponse response) throws IOException {
        Map<String, byte[]> args = new HashMap<>();
        args.put("hashId", hash.getBytes());
        Account account = xuperComponent.getAccount();
        XuperClient client = xuperComponent.getClient();
        Transaction tx = client.queryContract(account, "native", "javahashdeposit", "queryFileByHash", args);
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
        ServletOutputStream outputStream = response.getOutputStream();
        FileUtils.base64ToOutputStream(JSONObject.parseObject(tx.getContractResponse().getBodyStr()).getString("base64"), outputStream);
        outputStream.close();
    }
}
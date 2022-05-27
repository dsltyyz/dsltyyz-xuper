package com.dsltyyz.xuper.contract;

import com.alibaba.fastjson.JSONArray;
import com.baidu.xuper.*;

import java.util.*;

/**
 * HASH存证合约
 *
 * @author dsltyyz
 * @date 2022-5-26
 */
public class HashDepositContract implements Contract {

    private final String USER_ID = "userId";
    private final String HASH_ID = "hashId";
    private final String FILE = "file";
    private final String USER_PREFIX = "USER_";
    private final String HASH_PREFIX = "HASH_";

    @ContractMethod
    @Override
    public Response initialize(Context context) {
        return Response.ok("ok".getBytes());
    }

    @ContractMethod
    public Response storeFile(Context context) {
        byte[] userId = context.args().get(USER_ID);
        if (userId == null) {
            return Response.error("missing userId");
        }
        byte[] hashId = context.args().get(HASH_ID);
        if (hashId == null) {
            return Response.error("missing hashId");
        }
        byte[] file = context.args().get(FILE);
        if (file == null) {
            return Response.error("missing file");
        }
        String hashKey = HASH_PREFIX + new String(hashId);
        if (context.getObject(hashKey.getBytes()) != null) {
            return Response.error("the hashId is already exist, please check again");
        }
        context.putObject(hashKey.getBytes(), file);
        String userKey = USER_PREFIX + new String(userId) + "_" + new String(hashId);
        context.putObject(userKey.getBytes(), file);
        return Response.ok(hashId);
    }


    @ContractMethod
    public Response queryUserList(Context context) {
        Iterator<ContractIteratorItem> contractIteratorItemIterator = context.newIterator(USER_PREFIX.getBytes(), (USER_PREFIX + "~").getBytes());
        Set<String> set = new HashSet<>();
        while (contractIteratorItemIterator.hasNext()) {
            ContractIteratorItem next = contractIteratorItemIterator.next();
            set.add(new String(next.getKey()).substring(USER_PREFIX.length()).split("_")[0]);
        }
        return Response.ok(JSONArray.toJSONBytes(set));
    }

    @ContractMethod
    public Response queryFileByUser(Context context) {
        byte[] userId = context.args().get(USER_ID);
        if (userId == null) {
            return Response.error("missing userId");
        }
        String userKey = USER_PREFIX + new String(userId) + "_";
        Iterator<ContractIteratorItem> contractIteratorItemIterator = context.newIterator(userKey.getBytes(), (userKey + "~").getBytes());
        List<String> list = new ArrayList<>();
        while (contractIteratorItemIterator.hasNext()) {
            ContractIteratorItem next = contractIteratorItemIterator.next();
            list.add(new String(next.getKey()).substring(userKey.length()));
        }
        return Response.ok(JSONArray.toJSONBytes(list));
    }

    @ContractMethod
    public Response queryFileByHash(Context context) {
        byte[] hashId = context.args().get(HASH_ID);
        if (hashId == null) {
            return Response.error("missing hashId");
        }
        String userKey = HASH_PREFIX + new String(hashId);
        byte[] file = context.getObject(userKey.getBytes());
        if(file==null){
            return Response.error("queryFileByHash error");
        }
        return Response.ok(file);
    }

    public static void main(String[] args) {
        Driver.serve(new HashDepositContract());
    }

}

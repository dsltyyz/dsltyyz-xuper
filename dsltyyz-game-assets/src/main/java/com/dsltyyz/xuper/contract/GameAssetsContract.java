package com.dsltyyz.xuper.contract;

import com.alibaba.fastjson.JSONArray;
import com.baidu.xuper.*;

import java.util.*;

/**
 * 游戏资产合约
 *
 * @author dsltyyz
 * @date 2022-5-18
 */
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
        return Response.ok(JSONArray.toJSONBytes(list));
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
        return Response.ok(JSONArray.toJSONBytes(list));
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

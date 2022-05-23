package com.dsltyyz.xuper.contract;

import com.baidu.xuper.*;

/**
 * 自定义合约
 * @author dsltyyz
 * @date 2022-5-18
 */
public class DsltyyzContract implements Contract {

    @ContractMethod
    @Override
    public Response initialize(Context context) {
        return Response.ok("ok".getBytes());
    }

    @ContractMethod
    public Response calcFib(Context context){
        byte[] key = context.args().get("key");
        if (key == null) {
            return Response.error("missing key");
        }
        Integer n = new Integer(new String(key));
        Long fn = fib(n);
        return Response.ok(fn.toString().getBytes());
    }

    private Long fib(Integer n){
        if(n==1 ||n==2){
            return 1L;
        }else{
            return fib(n-1)+fib(n-2);
        }
    }

    public static void main(String[] args) {
        Driver.serve(new DsltyyzContract());
    }

}

package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.product.AddProductRequest;
import com.binance.platform.mbx.model.product.CreateAssetRequest;
import com.binance.platform.mbx.model.product.IceicebergLimitRequest;
import com.binance.platform.mbx.model.product.NumAlgoOrdersRequest;
import com.binance.platform.mbx.model.product.OrderTypeRequest;
import com.binance.platform.mbx.model.product.PriceConvertRequest;
import com.binance.platform.mbx.model.product.PriceConvertVo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Includes some default params for
 *
 * @author Li Fenggang
 * Date: 2020/8/4 5:06 下午
 */
public interface ProductClient {

    /**
     * 价格转换 - priceConvert
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/product/priceConvert")
    public PriceConvertVo priceConvert(PriceConvertRequest request)
            throws Exception;

    /**
     * 设置某个交易对的下单频率
     *
     * @param request
     * @throws Exception
     * @return
     */
    @PostMapping(value = "/mgmt/setNumOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> setNumAlgoOrdersFilter(NumAlgoOrdersRequest request)
            throws Exception;

    /**
     * 设置冰山单拆分数量
     *
     * @param request
     * @throws Exception
     * @return
     */
    @PostMapping(value = "/mgmt/setIceicebergLimit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> setIceicebergLimit(IceicebergLimitRequest request)
            throws Exception;

    /**
     * 设置下单类型
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/setProductType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> setProductType(OrderTypeRequest request) throws Exception;

    /**
     * 添加产品. Include some default params for product.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/addProduct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> addProduct(AddProductRequest request) throws Exception;

    /**
     * 创建Asset
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/creatAsset", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> creatAsset(CreateAssetRequest request) throws Exception;
}

package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.product.AddProductRequest;
import com.binance.platform.mbx.model.product.CreateAssetRequest;
import com.binance.platform.mbx.model.product.IceicebergLimitRequest;
import com.binance.platform.mbx.model.product.NumAlgoOrdersRequest;
import com.binance.platform.mbx.model.product.OrderTypeRequest;
import com.binance.platform.mbx.model.product.PriceConvertRequest;
import com.binance.platform.mbx.model.product.PriceConvertVo;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Li Fenggang
 * Date: 2020/7/20 8:44 上午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class ProductClientTest {
    private static final String DEFAULT_ASSET = "BTC";
    private static final String TARGET_ASSET = "BTC";
    private static final String DEFAULT_SYMBOL = "BTCUSDT";

    @Autowired
    private ProductClient productClient;

    // @Test
    public void priceConvert() throws Exception {
        PriceConvertRequest request = new PriceConvertRequest(DEFAULT_ASSET, TARGET_ASSET);
        PriceConvertVo priceConvert = productClient.priceConvert(request);
        System.out.println(priceConvert);
        Assert.assertNotNull(priceConvert);
    }

    // @Test
    public void setNumAlgoOrdersFilter() throws Exception {
        NumAlgoOrdersRequest request = new NumAlgoOrdersRequest(DEFAULT_SYMBOL, 10);
        MbxResponse<Void> mbxResponse = productClient.setNumAlgoOrdersFilter(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void setIceicebergLimit() throws Exception {
        IceicebergLimitRequest request = new IceicebergLimitRequest(DEFAULT_SYMBOL,10);
        MbxResponse<Void> mbxResponse = productClient.setIceicebergLimit(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void setProductType() throws Exception {
        OrderTypeRequest request = new OrderTypeRequest(DEFAULT_SYMBOL, true, true,
                true, true, true, true, true);
        MbxResponse<Void> mbxResponse = productClient.setProductType(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void addProduct() throws Exception {
        AddProductRequest request = new AddProductRequest(DEFAULT_SYMBOL, "BTC", "USDT");
        request.setBaseCommissionDecimalPlaces(8);
        request.setQuoteCommissionDecimalPlaces(8);
        MbxResponse<Void> mbxResponse = productClient.addProduct(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void creatAsset() throws Exception {
        CreateAssetRequest request = new CreateAssetRequest(DEFAULT_ASSET, "8");
        MbxResponse<Void> mbxResponse = productClient.creatAsset(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }
}

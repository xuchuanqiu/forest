package com.dtflys.test.http.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.DataParam;
import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Request;
import com.dtflys.test.model.*;
import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.test.model.Coordinate;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.DataParam;
import com.dtflys.test.model.Result;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 地图服务客户端接口
 * @author gongjun
 * @since 2016-06-01
 */
public interface AmapClient {

    @Request(
        url = "http://ditu.amap.com/service/regeo",
        dataType = "json"
    )
    Map getLocation(@DataParam("longitude") String longitude, @DataParam("latitude") String latitude);


    @Request(
        url = "http://ditu.amap.com/service/regeo",
        dataType = "json"
    )
    Map getLocation(@DataObject Coordinate coordinate);

    @Request(
            url = "http://ditu.amap.com/service/regeo",
            dataType = "json"
    )
    Result<Location> getLocationWithJavaObject(@DataObject Coordinate coordinate);

    @Request(
            url = "http://ditu.amap.com/service/regeo",
            dataType = "json"
    )
    Result<AmapLocation<AmapCross>> getLocationWithJavaObject2(@DataObject Coordinate coordinate);



    @Request(
        url = "http://ditu.amap.com/service/regeo",
        dataType = "json",
        data = {
            "longitude=${coord.longitude}",
            "latitude=${coord.latitude}"
        }
    )
    Map getLocationByCoordinate(@DataVariable("coord") Coordinate coordinate);


}

package com.mayisports.qca.bean;

import java.util.List;

/**
 * 赔率数据bean
 * Created by zhangpengju on 2018/6/11.
 */

public class MatchOddsBean {


    /**
     * data : {"match":{"betId":"1509873"},"analysis":[],"odds":{"europeOdds":[{"changeMinutes":325,"companyId":0,"companyName":"u5a01u5ec9u5e0cu5c14","companySeq":1,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":18,"minutes":55,"month":5,"seconds":3,"time":1528455303000,"timezoneOffset":-480,"year":118},"drowOdds":38394.7368,"drowRate":2448,"drowState":2,"firstDrowOdds":36026.3158,"firstDrowRate":2665,"firstLoseOdds":51763.1579,"firstLoseRate":1943,"firstReturnRate":9328,"firstWinOdds":16342.0526,"firstWinRate":5392,"flag":47,"lastUpdateTime":{"date":10,"day":0,"hours":17,"minutes":35,"month":5,"seconds":3,"time":1528623303000,"timezoneOffset":-480,"year":118},"loseOdds":59447.3684,"loseRate":1550,"loseState":1,"returnRate":9302,"winOdds":15268.3684,"winRate":6002,"winState":1,"history":"","title":"平均"},{"changeMinutes":325,"companyId":21,"companyName":"u5a01u5ec9u5e0cu5c14","companySeq":1,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":18,"minutes":55,"month":5,"seconds":3,"time":1528455303000,"timezoneOffset":-480,"year":118},"drowOdds":38000,"drowRate":2448,"drowState":2,"firstDrowOdds":35000,"firstDrowRate":2665,"firstLoseOdds":48000,"firstLoseRate":1943,"firstReturnRate":9328,"firstWinOdds":17300,"firstWinRate":5392,"flag":47,"lastUpdateTime":{"date":10,"day":0,"hours":17,"minutes":35,"month":5,"seconds":3,"time":1528623303000,"timezoneOffset":-480,"year":118},"loseOdds":60000,"loseRate":1550,"loseState":1,"returnRate":9302,"winOdds":15500,"winRate":6002,"winState":1,"history":"","title":"威廉"},{"changeMinutes":96,"companyId":23,"companyName":"u97e6u5fb7","companySeq":7,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":16,"minutes":22,"month":5,"seconds":2,"time":1528446122000,"timezoneOffset":-480,"year":118},"drowOdds":40000,"drowRate":2369,"drowState":0,"firstDrowOdds":34000,"firstDrowRate":2798,"firstLoseOdds":47500,"firstLoseRate":2003,"firstReturnRate":9514,"firstWinOdds":18300,"firstWinRate":5199,"flag":37,"lastUpdateTime":{"date":10,"day":0,"hours":21,"minutes":24,"month":5,"seconds":2,"time":1528637042000,"timezoneOffset":-480,"year":118},"loseOdds":62500,"loseRate":1516,"loseState":1,"returnRate":9477,"winOdds":15500,"winRate":6114,"winState":2,"history":"","title":"韦德"},{"changeMinutes":90,"companyId":26,"companyName":"u7acbu535a","companySeq":3,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":18,"minutes":45,"month":5,"seconds":1,"time":1528454701000,"timezoneOffset":-480,"year":118},"drowOdds":37500,"drowRate":2453,"drowState":0,"firstDrowOdds":34000,"firstDrowRate":2717,"firstLoseOdds":50000,"firstLoseRate":1848,"firstReturnRate":9239,"firstWinOdds":17000,"firstWinRate":5435,"flag":36,"lastUpdateTime":{"date":10,"day":0,"hours":21,"minutes":30,"month":5,"seconds":3,"time":1528637403000,"timezoneOffset":-480,"year":118},"loseOdds":60000,"loseRate":1533,"loseState":1,"returnRate":9200,"winOdds":15300,"winRate":6013,"winState":0,"history":"","title":"立博"},{"changeMinutes":1308,"companyId":24,"companyName":"Bet 365","companySeq":4,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":19,"minutes":8,"month":5,"seconds":1,"time":1528456081000,"timezoneOffset":-480,"year":118},"drowOdds":38000,"drowRate":2475,"drowState":1,"firstDrowOdds":38000,"firstDrowRate":2467,"firstLoseOdds":60000,"firstLoseRate":1562,"firstReturnRate":9374,"firstWinOdds":15700,"firstWinRate":5971,"flag":55,"lastUpdateTime":{"date":10,"day":0,"hours":1,"minutes":12,"month":5,"seconds":2,"time":1528564322000,"timezoneOffset":-480,"year":118},"loseOdds":75000,"loseRate":1254,"loseState":1,"returnRate":9406,"winOdds":15000,"winRate":6271,"winState":2,"history":"","title":"BET365"},{"changeMinutes":79,"companyId":140,"companyName":"Interwetten","companySeq":5,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":16,"minutes":18,"month":5,"seconds":2,"time":1528445882000,"timezoneOffset":-480,"year":118},"drowOdds":39000,"drowRate":2359,"drowState":1,"firstDrowOdds":34500,"firstDrowRate":2682,"firstLoseOdds":42500,"firstLoseRate":2177,"firstReturnRate":9253,"firstWinOdds":18000,"firstWinRate":5141,"flag":55,"lastUpdateTime":{"date":10,"day":0,"hours":21,"minutes":41,"month":5,"seconds":3,"time":1528638063000,"timezoneOffset":-480,"year":118},"loseOdds":61000,"loseRate":1508,"loseState":1,"returnRate":9200,"winOdds":15000,"winRate":6133,"winState":2,"history":"","title":"InterWett"},{"changeMinutes":2785,"companyId":215,"companyName":"Oddset","companySeq":12,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":17,"minutes":24,"month":5,"seconds":4,"time":1528449844000,"timezoneOffset":-480,"year":118},"drowOdds":35000,"drowRate":2479,"drowState":1,"firstDrowOdds":33000,"firstDrowRate":2617,"firstLoseOdds":37500,"firstLoseRate":2303,"firstReturnRate":8636,"firstWinOdds":17000,"firstWinRate":5080,"flag":55,"lastUpdateTime":{"date":9,"day":6,"hours":0,"minutes":35,"month":5,"seconds":1,"time":1528475701000,"timezoneOffset":-480,"year":118},"loseOdds":50000,"loseRate":1736,"loseState":1,"returnRate":8678,"winOdds":15000,"winRate":5785,"winState":2,"history":"","title":"Oddset"},{"changeMinutes":93,"companyId":174,"companyName":"SNAI","companySeq":10,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":19,"minutes":21,"month":5,"seconds":2,"time":1528456862000,"timezoneOffset":-480,"year":118},"drowOdds":39000,"drowRate":2367,"drowState":1,"firstDrowOdds":37500,"firstDrowRate":2456,"firstLoseOdds":57500,"firstLoseRate":1602,"firstReturnRate":9210,"firstWinOdds":15500,"firstWinRate":5942,"flag":19,"lastUpdateTime":{"date":10,"day":0,"hours":21,"minutes":27,"month":5,"seconds":3,"time":1528637223000,"timezoneOffset":-480,"year":118},"loseOdds":62500,"loseRate":1477,"loseState":0,"returnRate":9233,"winOdds":15000,"winRate":6155,"winState":2,"history":"","title":"SNAI"},{"changeMinutes":734,"companyId":196,"companyName":"u6fb3u95e8","companySeq":2,"companyType":"MAIN","createTime":{"date":10,"day":0,"hours":10,"minutes":46,"month":5,"seconds":2,"time":1528598762000,"timezoneOffset":-480,"year":118},"drowOdds":37500,"drowRate":2415,"drowState":0,"firstDrowOdds":37500,"firstDrowRate":2415,"firstLoseOdds":57000,"firstLoseRate":1589,"firstReturnRate":9055,"firstWinOdds":15100,"firstWinRate":5997,"flag":0,"lastUpdateTime":{"date":10,"day":0,"hours":10,"minutes":46,"month":5,"seconds":2,"time":1528598762000,"timezoneOffset":-480,"year":118},"loseOdds":57000,"loseRate":1589,"loseState":0,"returnRate":9055,"winOdds":15100,"winRate":5997,"winState":0,"history":"","title":"澳门"},{"changeMinutes":43,"companyId":1585,"companyName":"u7adeu5f69u5b98u65b9","companySeq":0,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":21,"minutes":33,"month":5,"seconds":33,"time":1528464813000,"timezoneOffset":-480,"year":118},"drowOdds":39000,"drowRate":2271,"drowState":1,"firstDrowOdds":37500,"firstDrowRate":2365,"firstLoseOdds":60000,"firstLoseRate":1478,"firstReturnRate":8867,"firstWinOdds":14400,"firstWinRate":6158,"flag":55,"lastUpdateTime":{"date":10,"day":0,"hours":22,"minutes":16,"month":5,"seconds":34,"time":1528640194000,"timezoneOffset":-480,"year":118},"loseOdds":67500,"loseRate":1312,"loseState":1,"returnRate":8855,"winOdds":13799,"winRate":6417,"winState":2,"history":"","title":"竞彩"},{"changeMinutes":49,"companyId":184,"companyName":"u6613u80dcu535a","companySeq":6,"companyType":"MAIN","createTime":{"date":10,"day":0,"hours":9,"minutes":37,"month":5,"seconds":3,"time":1528594623000,"timezoneOffset":-480,"year":118},"drowOdds":38000,"drowRate":2383,"drowState":2,"firstDrowOdds":38000,"firstDrowRate":2391,"firstLoseOdds":52000,"firstLoseRate":1747,"firstReturnRate":9086,"firstWinOdds":15500,"firstWinRate":5862,"flag":38,"lastUpdateTime":{"date":10,"day":0,"hours":22,"minutes":11,"month":5,"seconds":3,"time":1528639863000,"timezoneOffset":-480,"year":118},"loseOdds":51000,"loseRate":1775,"loseState":1,"returnRate":9055,"winOdds":15500,"winRate":5842,"winState":0,"history":"","title":"易胜博"},{"changeMinutes":1126,"companyId":162,"companyName":"bwin","companySeq":8,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":20,"minutes":8,"month":5,"seconds":1,"time":1528459681000,"timezoneOffset":-480,"year":118},"drowOdds":40000,"drowRate":2321,"drowState":2,"firstDrowOdds":40000,"firstDrowRate":2326,"firstLoseOdds":50000,"firstLoseRate":1860,"firstReturnRate":9302,"firstWinOdds":16000,"firstWinRate":5814,"flag":11,"lastUpdateTime":{"date":10,"day":0,"hours":4,"minutes":14,"month":5,"seconds":2,"time":1528575242000,"timezoneOffset":-480,"year":118},"loseOdds":55000,"loseRate":1688,"loseState":0,"returnRate":9285,"winOdds":15500,"winRate":5990,"winState":1,"history":"","title":"bwin"},{"changeMinutes":336,"companyId":177,"companyName":"u9999u6e2fu9a6cu4f1a","companySeq":11,"companyType":"MAIN","createTime":{"date":10,"day":0,"hours":17,"minutes":24,"month":5,"seconds":5,"time":1528622645000,"timezoneOffset":-480,"year":118},"drowOdds":38000,"drowRate":2341,"drowState":0,"firstDrowOdds":38000,"firstDrowRate":2341,"firstLoseOdds":60000,"firstLoseRate":1482,"firstReturnRate":8895,"firstWinOdds":14400,"firstWinRate":6177,"flag":0,"lastUpdateTime":{"date":10,"day":0,"hours":17,"minutes":24,"month":5,"seconds":5,"time":1528622645000,"timezoneOffset":-480,"year":118},"loseOdds":60000,"loseRate":1482,"loseState":0,"returnRate":8895,"winOdds":14400,"winRate":6177,"winState":0,"history":"","title":"香港马会"},{"changeMinutes":31,"companyId":15,"companyName":"10BET","companySeq":15,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":17,"minutes":54,"month":5,"seconds":2,"time":1528451642000,"timezoneOffset":-480,"year":118},"drowOdds":39000,"drowRate":2403,"drowState":2,"firstDrowOdds":35000,"firstDrowRate":2684,"firstLoseOdds":47500,"firstLoseRate":1978,"firstReturnRate":9395,"firstWinOdds":17600,"firstWinRate":5338,"flag":2,"lastUpdateTime":{"date":10,"day":0,"hours":22,"minutes":29,"month":5,"seconds":4,"time":1528640944000,"timezoneOffset":-480,"year":118},"loseOdds":62000,"loseRate":1512,"loseState":0,"returnRate":9372,"winOdds":15400,"winRate":6085,"winState":0,"history":"","title":"10bet"},{"changeMinutes":204,"companyId":20,"companyName":"u7687u51a0(Crown)","companySeq":18,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":19,"minutes":21,"month":5,"seconds":2,"time":1528456862000,"timezoneOffset":-480,"year":118},"drowOdds":40500,"drowRate":2347,"drowState":1,"firstDrowOdds":36000,"firstDrowRate":2563,"firstLoseOdds":50000,"firstLoseRate":1845,"firstReturnRate":9226,"firstWinOdds":16500,"firstWinRate":5592,"flag":31,"lastUpdateTime":{"date":10,"day":0,"hours":19,"minutes":36,"month":5,"seconds":2,"time":1528630562000,"timezoneOffset":-480,"year":118},"loseOdds":58000,"loseRate":1639,"loseState":2,"returnRate":9504,"winOdds":15800,"winRate":6015,"winState":1,"history":"","title":"皇冠"},{"changeMinutes":0,"companyId":18,"companyName":"u660eu965e","companySeq":19,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":19,"minutes":30,"month":5,"seconds":2,"time":1528457402000,"timezoneOffset":-480,"year":118},"drowOdds":37500,"drowRate":2473,"drowState":1,"firstDrowOdds":36000,"firstDrowRate":2571,"firstLoseOdds":54000,"firstLoseRate":1714,"firstReturnRate":9257,"firstWinOdds":16200,"firstWinRate":5714,"flag":22,"lastUpdateTime":{"date":10,"day":0,"hours":23,"minutes":0,"month":5,"seconds":5,"time":1528642805000,"timezoneOffset":-480,"year":118},"loseOdds":56000,"loseRate":1656,"loseState":2,"returnRate":9275,"winOdds":15800,"winRate":5870,"winState":0,"history":"","title":"明陞"}]},"status":0}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * match : {"betId":"1509873"}
         * analysis : []
         * odds : {"europeOdds":[{"changeMinutes":325,"companyId":0,"companyName":"u5a01u5ec9u5e0cu5c14","companySeq":1,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":18,"minutes":55,"month":5,"seconds":3,"time":1528455303000,"timezoneOffset":-480,"year":118},"drowOdds":38394.7368,"drowRate":2448,"drowState":2,"firstDrowOdds":36026.3158,"firstDrowRate":2665,"firstLoseOdds":51763.1579,"firstLoseRate":1943,"firstReturnRate":9328,"firstWinOdds":16342.0526,"firstWinRate":5392,"flag":47,"lastUpdateTime":{"date":10,"day":0,"hours":17,"minutes":35,"month":5,"seconds":3,"time":1528623303000,"timezoneOffset":-480,"year":118},"loseOdds":59447.3684,"loseRate":1550,"loseState":1,"returnRate":9302,"winOdds":15268.3684,"winRate":6002,"winState":1,"history":"","title":"平均"},{"changeMinutes":325,"companyId":21,"companyName":"u5a01u5ec9u5e0cu5c14","companySeq":1,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":18,"minutes":55,"month":5,"seconds":3,"time":1528455303000,"timezoneOffset":-480,"year":118},"drowOdds":38000,"drowRate":2448,"drowState":2,"firstDrowOdds":35000,"firstDrowRate":2665,"firstLoseOdds":48000,"firstLoseRate":1943,"firstReturnRate":9328,"firstWinOdds":17300,"firstWinRate":5392,"flag":47,"lastUpdateTime":{"date":10,"day":0,"hours":17,"minutes":35,"month":5,"seconds":3,"time":1528623303000,"timezoneOffset":-480,"year":118},"loseOdds":60000,"loseRate":1550,"loseState":1,"returnRate":9302,"winOdds":15500,"winRate":6002,"winState":1,"history":"","title":"威廉"},{"changeMinutes":96,"companyId":23,"companyName":"u97e6u5fb7","companySeq":7,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":16,"minutes":22,"month":5,"seconds":2,"time":1528446122000,"timezoneOffset":-480,"year":118},"drowOdds":40000,"drowRate":2369,"drowState":0,"firstDrowOdds":34000,"firstDrowRate":2798,"firstLoseOdds":47500,"firstLoseRate":2003,"firstReturnRate":9514,"firstWinOdds":18300,"firstWinRate":5199,"flag":37,"lastUpdateTime":{"date":10,"day":0,"hours":21,"minutes":24,"month":5,"seconds":2,"time":1528637042000,"timezoneOffset":-480,"year":118},"loseOdds":62500,"loseRate":1516,"loseState":1,"returnRate":9477,"winOdds":15500,"winRate":6114,"winState":2,"history":"","title":"韦德"},{"changeMinutes":90,"companyId":26,"companyName":"u7acbu535a","companySeq":3,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":18,"minutes":45,"month":5,"seconds":1,"time":1528454701000,"timezoneOffset":-480,"year":118},"drowOdds":37500,"drowRate":2453,"drowState":0,"firstDrowOdds":34000,"firstDrowRate":2717,"firstLoseOdds":50000,"firstLoseRate":1848,"firstReturnRate":9239,"firstWinOdds":17000,"firstWinRate":5435,"flag":36,"lastUpdateTime":{"date":10,"day":0,"hours":21,"minutes":30,"month":5,"seconds":3,"time":1528637403000,"timezoneOffset":-480,"year":118},"loseOdds":60000,"loseRate":1533,"loseState":1,"returnRate":9200,"winOdds":15300,"winRate":6013,"winState":0,"history":"","title":"立博"},{"changeMinutes":1308,"companyId":24,"companyName":"Bet 365","companySeq":4,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":19,"minutes":8,"month":5,"seconds":1,"time":1528456081000,"timezoneOffset":-480,"year":118},"drowOdds":38000,"drowRate":2475,"drowState":1,"firstDrowOdds":38000,"firstDrowRate":2467,"firstLoseOdds":60000,"firstLoseRate":1562,"firstReturnRate":9374,"firstWinOdds":15700,"firstWinRate":5971,"flag":55,"lastUpdateTime":{"date":10,"day":0,"hours":1,"minutes":12,"month":5,"seconds":2,"time":1528564322000,"timezoneOffset":-480,"year":118},"loseOdds":75000,"loseRate":1254,"loseState":1,"returnRate":9406,"winOdds":15000,"winRate":6271,"winState":2,"history":"","title":"BET365"},{"changeMinutes":79,"companyId":140,"companyName":"Interwetten","companySeq":5,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":16,"minutes":18,"month":5,"seconds":2,"time":1528445882000,"timezoneOffset":-480,"year":118},"drowOdds":39000,"drowRate":2359,"drowState":1,"firstDrowOdds":34500,"firstDrowRate":2682,"firstLoseOdds":42500,"firstLoseRate":2177,"firstReturnRate":9253,"firstWinOdds":18000,"firstWinRate":5141,"flag":55,"lastUpdateTime":{"date":10,"day":0,"hours":21,"minutes":41,"month":5,"seconds":3,"time":1528638063000,"timezoneOffset":-480,"year":118},"loseOdds":61000,"loseRate":1508,"loseState":1,"returnRate":9200,"winOdds":15000,"winRate":6133,"winState":2,"history":"","title":"InterWett"},{"changeMinutes":2785,"companyId":215,"companyName":"Oddset","companySeq":12,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":17,"minutes":24,"month":5,"seconds":4,"time":1528449844000,"timezoneOffset":-480,"year":118},"drowOdds":35000,"drowRate":2479,"drowState":1,"firstDrowOdds":33000,"firstDrowRate":2617,"firstLoseOdds":37500,"firstLoseRate":2303,"firstReturnRate":8636,"firstWinOdds":17000,"firstWinRate":5080,"flag":55,"lastUpdateTime":{"date":9,"day":6,"hours":0,"minutes":35,"month":5,"seconds":1,"time":1528475701000,"timezoneOffset":-480,"year":118},"loseOdds":50000,"loseRate":1736,"loseState":1,"returnRate":8678,"winOdds":15000,"winRate":5785,"winState":2,"history":"","title":"Oddset"},{"changeMinutes":93,"companyId":174,"companyName":"SNAI","companySeq":10,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":19,"minutes":21,"month":5,"seconds":2,"time":1528456862000,"timezoneOffset":-480,"year":118},"drowOdds":39000,"drowRate":2367,"drowState":1,"firstDrowOdds":37500,"firstDrowRate":2456,"firstLoseOdds":57500,"firstLoseRate":1602,"firstReturnRate":9210,"firstWinOdds":15500,"firstWinRate":5942,"flag":19,"lastUpdateTime":{"date":10,"day":0,"hours":21,"minutes":27,"month":5,"seconds":3,"time":1528637223000,"timezoneOffset":-480,"year":118},"loseOdds":62500,"loseRate":1477,"loseState":0,"returnRate":9233,"winOdds":15000,"winRate":6155,"winState":2,"history":"","title":"SNAI"},{"changeMinutes":734,"companyId":196,"companyName":"u6fb3u95e8","companySeq":2,"companyType":"MAIN","createTime":{"date":10,"day":0,"hours":10,"minutes":46,"month":5,"seconds":2,"time":1528598762000,"timezoneOffset":-480,"year":118},"drowOdds":37500,"drowRate":2415,"drowState":0,"firstDrowOdds":37500,"firstDrowRate":2415,"firstLoseOdds":57000,"firstLoseRate":1589,"firstReturnRate":9055,"firstWinOdds":15100,"firstWinRate":5997,"flag":0,"lastUpdateTime":{"date":10,"day":0,"hours":10,"minutes":46,"month":5,"seconds":2,"time":1528598762000,"timezoneOffset":-480,"year":118},"loseOdds":57000,"loseRate":1589,"loseState":0,"returnRate":9055,"winOdds":15100,"winRate":5997,"winState":0,"history":"","title":"澳门"},{"changeMinutes":43,"companyId":1585,"companyName":"u7adeu5f69u5b98u65b9","companySeq":0,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":21,"minutes":33,"month":5,"seconds":33,"time":1528464813000,"timezoneOffset":-480,"year":118},"drowOdds":39000,"drowRate":2271,"drowState":1,"firstDrowOdds":37500,"firstDrowRate":2365,"firstLoseOdds":60000,"firstLoseRate":1478,"firstReturnRate":8867,"firstWinOdds":14400,"firstWinRate":6158,"flag":55,"lastUpdateTime":{"date":10,"day":0,"hours":22,"minutes":16,"month":5,"seconds":34,"time":1528640194000,"timezoneOffset":-480,"year":118},"loseOdds":67500,"loseRate":1312,"loseState":1,"returnRate":8855,"winOdds":13799,"winRate":6417,"winState":2,"history":"","title":"竞彩"},{"changeMinutes":49,"companyId":184,"companyName":"u6613u80dcu535a","companySeq":6,"companyType":"MAIN","createTime":{"date":10,"day":0,"hours":9,"minutes":37,"month":5,"seconds":3,"time":1528594623000,"timezoneOffset":-480,"year":118},"drowOdds":38000,"drowRate":2383,"drowState":2,"firstDrowOdds":38000,"firstDrowRate":2391,"firstLoseOdds":52000,"firstLoseRate":1747,"firstReturnRate":9086,"firstWinOdds":15500,"firstWinRate":5862,"flag":38,"lastUpdateTime":{"date":10,"day":0,"hours":22,"minutes":11,"month":5,"seconds":3,"time":1528639863000,"timezoneOffset":-480,"year":118},"loseOdds":51000,"loseRate":1775,"loseState":1,"returnRate":9055,"winOdds":15500,"winRate":5842,"winState":0,"history":"","title":"易胜博"},{"changeMinutes":1126,"companyId":162,"companyName":"bwin","companySeq":8,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":20,"minutes":8,"month":5,"seconds":1,"time":1528459681000,"timezoneOffset":-480,"year":118},"drowOdds":40000,"drowRate":2321,"drowState":2,"firstDrowOdds":40000,"firstDrowRate":2326,"firstLoseOdds":50000,"firstLoseRate":1860,"firstReturnRate":9302,"firstWinOdds":16000,"firstWinRate":5814,"flag":11,"lastUpdateTime":{"date":10,"day":0,"hours":4,"minutes":14,"month":5,"seconds":2,"time":1528575242000,"timezoneOffset":-480,"year":118},"loseOdds":55000,"loseRate":1688,"loseState":0,"returnRate":9285,"winOdds":15500,"winRate":5990,"winState":1,"history":"","title":"bwin"},{"changeMinutes":336,"companyId":177,"companyName":"u9999u6e2fu9a6cu4f1a","companySeq":11,"companyType":"MAIN","createTime":{"date":10,"day":0,"hours":17,"minutes":24,"month":5,"seconds":5,"time":1528622645000,"timezoneOffset":-480,"year":118},"drowOdds":38000,"drowRate":2341,"drowState":0,"firstDrowOdds":38000,"firstDrowRate":2341,"firstLoseOdds":60000,"firstLoseRate":1482,"firstReturnRate":8895,"firstWinOdds":14400,"firstWinRate":6177,"flag":0,"lastUpdateTime":{"date":10,"day":0,"hours":17,"minutes":24,"month":5,"seconds":5,"time":1528622645000,"timezoneOffset":-480,"year":118},"loseOdds":60000,"loseRate":1482,"loseState":0,"returnRate":8895,"winOdds":14400,"winRate":6177,"winState":0,"history":"","title":"香港马会"},{"changeMinutes":31,"companyId":15,"companyName":"10BET","companySeq":15,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":17,"minutes":54,"month":5,"seconds":2,"time":1528451642000,"timezoneOffset":-480,"year":118},"drowOdds":39000,"drowRate":2403,"drowState":2,"firstDrowOdds":35000,"firstDrowRate":2684,"firstLoseOdds":47500,"firstLoseRate":1978,"firstReturnRate":9395,"firstWinOdds":17600,"firstWinRate":5338,"flag":2,"lastUpdateTime":{"date":10,"day":0,"hours":22,"minutes":29,"month":5,"seconds":4,"time":1528640944000,"timezoneOffset":-480,"year":118},"loseOdds":62000,"loseRate":1512,"loseState":0,"returnRate":9372,"winOdds":15400,"winRate":6085,"winState":0,"history":"","title":"10bet"},{"changeMinutes":204,"companyId":20,"companyName":"u7687u51a0(Crown)","companySeq":18,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":19,"minutes":21,"month":5,"seconds":2,"time":1528456862000,"timezoneOffset":-480,"year":118},"drowOdds":40500,"drowRate":2347,"drowState":1,"firstDrowOdds":36000,"firstDrowRate":2563,"firstLoseOdds":50000,"firstLoseRate":1845,"firstReturnRate":9226,"firstWinOdds":16500,"firstWinRate":5592,"flag":31,"lastUpdateTime":{"date":10,"day":0,"hours":19,"minutes":36,"month":5,"seconds":2,"time":1528630562000,"timezoneOffset":-480,"year":118},"loseOdds":58000,"loseRate":1639,"loseState":2,"returnRate":9504,"winOdds":15800,"winRate":6015,"winState":1,"history":"","title":"皇冠"},{"changeMinutes":0,"companyId":18,"companyName":"u660eu965e","companySeq":19,"companyType":"MAIN","createTime":{"date":8,"day":5,"hours":19,"minutes":30,"month":5,"seconds":2,"time":1528457402000,"timezoneOffset":-480,"year":118},"drowOdds":37500,"drowRate":2473,"drowState":1,"firstDrowOdds":36000,"firstDrowRate":2571,"firstLoseOdds":54000,"firstLoseRate":1714,"firstReturnRate":9257,"firstWinOdds":16200,"firstWinRate":5714,"flag":22,"lastUpdateTime":{"date":10,"day":0,"hours":23,"minutes":0,"month":5,"seconds":5,"time":1528642805000,"timezoneOffset":-480,"year":118},"loseOdds":56000,"loseRate":1656,"loseState":2,"returnRate":9275,"winOdds":15800,"winRate":5870,"winState":0,"history":"","title":"明陞"}]}
         * status : 0
         */
        public OddsBean odds;
        public int status;


        public static class OddsBean {
            public List<EuropeOddsBean> europeOdds;

            public List<AsiaBean> asia;

            public List<ScoreBean> score;

            public static class ScoreBean {
                /**
                 * id : 1
                 * companyId : 1
                 * companyName : u660eu965e
                 * companyType : MAIN
                 * companySeq : 1
                 * firstTape : 22500
                 * firstHostOdds : 9000
                 * firstAwayOdds : 10000
                 * tape : 25000
                 * hostOdds : 10000
                 * awayOdds : 9000
                 * tapeState : 0
                 * hostOddsState : 1
                 * awayOddsState : 2
                 * winOdds : 15800
                 * drowOdds : 37500
                 * loseOdds : 56000
                 * flag : 88
                 * lastUpdateTime : 0610 23:08
                 * changeMinutes : 1
                 * createTime : 0608 17:20
                 * history :
                 * title : 明陞
                 */

                public String id;
                public String companyId;
                public String companyName;
                public String companyType;
                public String companySeq;
                public String firstTape;
                public String firstHostOdds;
                public String firstAwayOdds;
                public String tape;
                public String hostOdds;
                public String awayOdds;
                public String tapeState;
                public String hostOddsState;
                public String awayOddsState;
                public String winOdds;
                public String drowOdds;
                public String loseOdds;
                public String flag;
                public String lastUpdateTime;
                public String changeMinutes;
                public String createTime;
                public String history;
                public String title;
            }

            public static class AsiaBean {
                /**
                 * firstTape : 5000
                 * companyId : 2
                 * tape : 10000
                 * firstHostOdds : 0.80
                 * firstAwayOdds : 1.14
                 * hostOdds : 1.03
                 * awayOdds : 0.89
                 * winOdds : 1.58
                 * drowOdds : 3.75
                 * loseOdds : 5.60
                 * createTime : 0608 17:21
                 * createTimeNum : 1528449675
                 * history :
                 * lastUpdateTime : 0610 22:50
                 * lastUpdateTimeNum : 1528642257
                 * title : 沙巴
                 */

                public String firstTape;
                public String companyId;
                public String tape;
                public String firstHostOdds;
                public String firstAwayOdds;
                public String hostOdds;
                public String awayOdds;
                public String winOdds;
                public String drowOdds;
                public String loseOdds;
                public String createTime;
                public int createTimeNum;
                public String history;
                public String lastUpdateTime;
                public int lastUpdateTimeNum;
                public String title;
            }

            public static class EuropeOddsBean {
                /**
                 * changeMinutes : 325
                 * companyId : 0
                 * companyName : u5a01u5ec9u5e0cu5c14
                 * companySeq : 1
                 * companyType : MAIN
                 * createTime : {"date":8,"day":5,"hours":18,"minutes":55,"month":5,"seconds":3,"time":1528455303000,"timezoneOffset":-480,"year":118}
                 * drowOdds : 38394.7368
                 * drowRate : 2448
                 * drowState : 2
                 * firstDrowOdds : 36026.3158
                 * firstDrowRate : 2665
                 * firstLoseOdds : 51763.1579
                 * firstLoseRate : 1943
                 * firstReturnRate : 9328
                 * firstWinOdds : 16342.0526
                 * firstWinRate : 5392
                 * flag : 47
                 * lastUpdateTime : {"date":10,"day":0,"hours":17,"minutes":35,"month":5,"seconds":3,"time":1528623303000,"timezoneOffset":-480,"year":118}
                 * loseOdds : 59447.3684
                 * loseRate : 1550
                 * loseState : 1
                 * returnRate : 9302
                 * winOdds : 15268.3684
                 * winRate : 6002
                 * winState : 1
                 * history :
                 * title : 平均
                 */

                public int changeMinutes;
                public int companyId;
                public String companyName;
                public int companySeq;
                public String companyType;
                public CreateTimeBean createTime;
                public double drowOdds;
                public int drowRate;
                public int drowState;
                public double firstDrowOdds;
                public int firstDrowRate;
                public double firstLoseOdds;
                public int firstLoseRate;
                public int firstReturnRate;
                public double firstWinOdds;
                public int firstWinRate;
                public int flag;
                public LastUpdateTimeBean lastUpdateTime;
                public double loseOdds;
                public int loseRate;
                public int loseState;
                public int returnRate;
                public double winOdds;
                public int winRate;
                public int winState;
                public String history;
                public String title;

                public static class CreateTimeBean {
                    /**
                     * date : 8
                     * day : 5
                     * hours : 18
                     * minutes : 55
                     * month : 5
                     * seconds : 3
                     * time : 1528455303000
                     * timezoneOffset : -480
                     * year : 118
                     */

                    public int date;
                    public int day;
                    public int hours;
                    public int minutes;
                    public int month;
                    public int seconds;
                    public long time;
                    public int timezoneOffset;
                    public int year;
                }

                public static class LastUpdateTimeBean {
                    /**
                     * date : 10
                     * day : 0
                     * hours : 17
                     * minutes : 35
                     * month : 5
                     * seconds : 3
                     * time : 1528623303000
                     * timezoneOffset : -480
                     * year : 118
                     */

                    public int date;
                    public int day;
                    public int hours;
                    public int minutes;
                    public int month;
                    public int seconds;
                    public long time;
                    public int timezoneOffset;
                    public int year;
                }
            }
        }
    }
}

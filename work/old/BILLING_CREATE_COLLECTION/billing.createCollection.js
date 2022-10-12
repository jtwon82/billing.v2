/**	목적 : billing collection 생성	**/
/**	행위 : 1주일에 한번씩 생성	**/
/** 이용 collection : billing_yyyyMMdd	**/
/**	생성일 :	**/
/**	생성자 :	**/
/**	변경일(최종) :	**/
/**	변경자(최종) :	**/
/**	변경사항(최종) :	**/
print('**Begin**************************************************');
print("Time : " + new Date().toISOString());
var collPrefix = "billing_";
var dates = 3;
var now = new Date();
var today = new Date(now);
today.setHours(0, 0, 0, 0);
var sToday = today.toISOString().substr(0,10);
var begin = new Date(today);
begin.setDate(begin.getDate());
var collections = db.getCollectionNames();
print(JSON.stringify(collections));
for(var idx=0; idx <= dates; idx++){
        if(idx !== 0) {
                begin.setHours(begin.getDate() + 1);
        }
        var newCollName = collPrefix+begin.toISOString().substr(0,10).replace(/-/g,"");
        if(Array.contains(collections, newCollName)) {
                continue;
        }
        db.createCollection(newCollName);
        db[newCollName].createIndex( {"offset_partition":1}, { unique: true });
        sh.shardCollection(newCollName + ".",  {"offset_partition":"hashed"});
        print("Created Collection["+newCollName+"]");
}
print("Time : " + new Date().toISOString());
print('**End**************************************************');
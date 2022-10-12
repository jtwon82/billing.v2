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
var collPrefix = "conv_info_";
var dates = 3;
var today = new Date();
var collections = db.getCollectionNames();

print("collections - "+ collections);
for( var i=dates; i>=0; i--){
	var begin = new Date(today.getFullYear(), today.getMonth(), today.getDate()+i, today.getHours(), today.getMinutes(), today.getSeconds());
	var newCollName = collPrefix + begin.toISOString().substr(0,10).replace(/-/g,"");
	if(Array.contains(collections, newCollName)) {
		continue;
	}

	print(">> Start "+ newCollName);
	db.createCollection(newCollName);

	try{
		sh.shardCollection("billing."+ newCollName, {"_id":"hashed"});
	}catch(e){}
	//db[newCollName].createIndex( {"insertDate":-1} );
	db[newCollName].createIndex( {"insertDate":-1, "platform":1} );

	print("Created Collection["+newCollName+"]");
}
print("Time : " + new Date().toISOString());
print('**End**************************************************');

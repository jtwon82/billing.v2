/**	목적 : billing collection 생성	**/
/**	행위 : 1주일에 한번씩 생성	**/
/** 이용 collection : billing_yyyyMMdd	**/
/**	생성일 :	**/
/**	생성자 :	**/
/**	변경일(최종) :	**/
/**	변경자(최종) :	**/
/**	변경사항(최종) :	**/
// mongo 127.0.01:27017/billing --eval "load('/workset/workspace/mobon-kafka-batch/src/doc/BILLING_CREATE_COLLECTION/billing.dropCollection.js')"
print('**Begin**************************************************');
print("Time : " + new Date().toISOString());
var chkingDay = -20;
var today = new Date();
var collections = db.getCollectionNames();

print("Collections - "+ collections);
print("");

collections.forEach(function(element, index, array){
	for( var i=-3; i>=chkingDay; i--){
		var begin = new Date(today.getFullYear(), today.getMonth(), today.getDate()+i, today.getHours(), today.getMinutes(), today.getSeconds());
		var targetDate = begin.toISOString().substr(0,10).replace(/-/g,"");
		
		if( element.indexOf(targetDate)>-1 ){
			print("element-"+ element +" drop-"+ targetDate);
			db[element].drop();
		}
	}
});

print("Time : " + new Date().toISOString());
print('**End**************************************************');

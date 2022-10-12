/**	목적 : pre-chunk수행	**/
/**	행위 : preChun메소드 로딩(수행지에서 함수이용)	**/
/** 이용 db : admin	**/
/**	생성일 :	**/
/**	생성자 :	**/
/**	변경일(최종) :	**/
/**	변경자(최종) :	**/
/**	변경사항(최종) :	**/

var isEmpty = function(val) {
	return val == '' || val == null || val == undefined || (val != null && typeof val == 'object' && !Object.keys(val).length);
};
var preChunk = function(ip, port, dbName, collNames, reqSplitCnt) {
	print('');
	print('**Begin*PreCheck*************************************************');
	print('Time : ' + new Date().toISOString());
	print('IP[' + ip + ']');
	print('PORT[' + port + ']');
	print('DB NAME[' + dbName + ']');
	print('COLLECTION NAMES[' + JSON.stringify(collNames) + ']');
	print('REQUEST SPLIT PRE-CHUNK CNT[' + reqSplitCnt + ']');
	if(isEmpty(ip) || isEmpty(port) || isEmpty(dbName) || isEmpty(collNames)) {
		print('ERROR : Info is empty');
	} else{
		/** DB Admin Prepare **/
		var adminDb = connect(ip+ ':' + port + '/admin');
		/** preChunk Prepare **/
		var splitCnt = Math.ceil(reqSplitCnt/2);
		var max = Math.pow(2, 63) -1;
		var division = Math.round(max / splitCnt);
		/** Work **/
		for( var i = 0; i < collNames.length; i++) {
			var dbCollName = dbName + '.' + collNames[i];
			print('PRE-CHUNK COLLECTION[' + dbCollName + ']');
			var procCnt = 0;
			for( var x=0;  x>=0 && x<max; x+=division ) {
				adminDb.runCommand( { split : dbCollName , middle : { _id : NumberLong(-1 * x) } } );
				adminDb.runCommand( { split : dbCollName , middle : { _id : NumberLong(x) } } );
				procCnt+=2;
			}
			print('PRE-CHUNK CNT => ' + procCnt);
		}
	}
	print("Time : " + new Date().toISOString());
	print('**End*PreCheck*************************************************');
};

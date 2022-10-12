/**	목적 : mongos별 flushRounterConfig 처리	**/
/**	행위 : mongos별 flushRounterConfig 처리	**/
/** 이용 db : admin	**/
/**	생성일 :	**/
/**	생성자 :	**/
/**	변경일(최종) :	**/
/**	변경자(최종) :	**/
/**	변경사항(최종) :	**/
var isEmpty = function(val) {
	return val == '' || val == null || val == undefined || (val != null && typeof val == 'object' && !Object.keys(val).length);
};
var mongoAdminCmd = function(ip, port, cmdData) {
	print('**Begin*PreCheck*************************************************');
	print('Time : ' + new Date().toISOString());
	print('IP[' + ip + ']');
	print('PORT[' + port + ']');
	print('CMD DATA[' + cmdData + ']');
	if(isEmpty(ip) || isEmpty(port)) {
		print('ERROR : Info is empty');
	} else{
		/** DB Admin Prepare **/
		try{
			var adminDb = connect(ip+ ':' + port + '/admin');
			var res = adminDb.runCommand(cmdData);
			print('RES[' + JSON.stringify(res) + ']');
		}catch(err){
			print('[ERROR]' + ERR.message );
		}
	}
	print("Time : " + new Date().toISOString());
	print('**End*PreCheck*************************************************');
};
mongoAdminCmd('192.168.2.78', '10001', 'flushRouterConfig');
mongoAdminCmd('192.168.2.78', '10002', 'flushRouterConfig');
mongoAdminCmd('192.168.2.79', '10001', 'flushRouterConfig');
mongoAdminCmd('192.168.2.79', '10002', 'flushRouterConfig');

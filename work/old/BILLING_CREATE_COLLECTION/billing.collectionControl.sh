
mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.click_view.js');" >> /home/dreamsearch/logs/cron/billing.createCollection.log.`date +%y%m` 2>&1; echo '1/1E.ok'

mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/billing.createCollection.conv_info.js')" >> /home/dreamsearch/logs/cron/billing.createCollection.log.`date +%y%m` 2>&1; echo '1/1E.ok'

#mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.shop_info.js');" >> /home/dreamsearch/logs/cron/billing.createCollection.log.`date +%y%m` 2>&1; echo '1/1E.ok'

mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.shop_stats.js');" >> /home/dreamsearch/logs/cron/billing.createCollection.log.`date +%y%m` 2>&1; echo '1/1E.ok'

mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.external_info.js');" >> /home/dreamsearch/logs/cron/billing.createCollection.log.`date +%y%m` 2>&1; echo '1/1E.ok'

mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.insert_maria_error.js');" >> /home/dreamsearch/logs/cron/billing.createCollection.insert_maria_error.log.`date +%y%m` 2>&1; echo '1/1E.ok'

mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/billing.dropCollection.js')" >> /home/dreamsearch/logs/cron/billing.dropCollection.log.`date +%y%m` 2>&1; echo '1/1E.ok'

mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/mongosFlushRouterConfig.js')" >> /home/dreamsearch/logs/cron/mongosFlushRouterConfig.log.`date +%y%m` 2>&1; echo '1/1E.ok'








운영
mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.click_view.js')"
mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.conv_info.js')"
mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.shop_info.js')"
mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.shop_stats.js')"
mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.external_info.js')"
mongo 127.0.0.1:10001/billing --eval "load('/home/dreamsearch/scripts/preChunk.js'); load('/home/dreamsearch/scripts/billing.createCollection.insert_maria_error.js')"


mongo 127.0.0.1:27017/billing --eval "load('/home/dreamsearch/scripts/billing.createCollection.click_view.js')"
mongo 127.0.0.1:27017/billing --eval "load('/home/dreamsearch/scripts/billing.createCollection.conv_info.js')"
mongo 127.0.0.1:27017/billing --eval "load('/home/dreamsearch/scripts/billing.createCollection.shop_info.js')"
mongo 127.0.0.1:27017/billing --eval "load('/home/dreamsearch/scripts/billing.createCollection.shop_stats.js')"
mongo 127.0.0.1:27017/billing --eval "load('/home/dreamsearch/scripts/billing.createCollection.external_info.js')"
mongo 127.0.0.1:27017/billing --eval "load('/home/dreamsearch/scripts/billing.dropCollection.js')"

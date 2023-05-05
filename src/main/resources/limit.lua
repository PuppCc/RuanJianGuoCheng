-- 减少网络开销: 不使用 Lua 的代码需要向 Redis 发送多次请求, 而脚本只需一次即可, 减少网络传输
-- 原子操作: Redis 将整个脚本作为一个原子执行, 无需担心并发, 也就无需事务
-- 复用: 脚本会永久保存 Redis 中, 其他客户端可继续使用

local key = "rate.limit:" .. KEYS[1] --限流KEY
local limit = tonumber(ARGV[1])      --限流大小
local cycle = ARGV[2]                --过期周期
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then --如果超出限流大小
   return 0
else  --请求数+1，并设置过期时间
   redis.call("INCRBY", key, "1")
   redis.call("expire", key, cycle)
   return current + 1
end



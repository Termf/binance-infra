local key     = KEYS[1]
local thread  = ARGV[1]
local value = redis.call('get', key)
if value == thread then
  return redis.call('del', key);
end
return 1
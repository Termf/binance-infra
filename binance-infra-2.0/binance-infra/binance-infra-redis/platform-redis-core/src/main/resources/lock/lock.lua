local key     = KEYS[1]
local thread  = ARGV[1]
local ttl     = ARGV[2]
local lockSet = redis.call('setnx', key, thread)
if lockSet == 1 then
  redis.call('pexpire', key, ttl)
else 
  local value = redis.call('get', key)
  if(value == thread) then
    lockSet = 1;
    redis.call('pexpire', key, ttl)
  end
end
return lockSet
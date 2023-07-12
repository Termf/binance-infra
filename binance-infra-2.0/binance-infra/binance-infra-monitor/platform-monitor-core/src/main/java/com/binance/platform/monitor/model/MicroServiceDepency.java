package com.binance.platform.monitor.model;

import java.util.Set;

import com.google.common.collect.Sets;

public class MicroServiceDepency {

    private String serviceId;

    private Set<CallMicorService> calls;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Set<CallMicorService> addCall(CallMicorService service) {
        if (this.calls == null) {
            this.calls = Sets.newHashSet();
        }
        for (CallMicorService callService : calls) {
            if (callService.equals(service)) {
                callService.addCount();
            }
        }
        this.calls.add(service);
        return this.calls;
    }

    public Set<CallMicorService> getCalls() {
        return calls;
    }

    public void setCalls(Set<CallMicorService> calls) {
        this.calls = calls;
    }

    public class CallMicorService {
        private String source;
        private String target;
        private String callType;
        private Integer callCount;

        public CallMicorService(String source, String target) {
            this.source = source;
            this.target = target;
            this.callCount = 1;
            this.callType = "openfeign";
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getCallType() {
            return callType;
        }

        public void setCallType(String callType) {
            this.callType = callType;
        }

        public Integer getCallCount() {
            return callCount;
        }

        public void setCallCount(Integer callCount) {
            this.callCount = callCount;
        }

        public void addCount() {
            this.callCount++;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((source == null) ? 0 : source.hashCode());
            result = prime * result + ((target == null) ? 0 : target.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CallMicorService other = (CallMicorService)obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (source == null) {
                if (other.source != null)
                    return false;
            } else if (!source.equals(other.source))
                return false;
            if (target == null) {
                if (other.target != null)
                    return false;
            } else if (!target.equals(other.target))
                return false;
            return true;
        }

        private MicroServiceDepency getEnclosingInstance() {
            return MicroServiceDepency.this;
        }

    }
}
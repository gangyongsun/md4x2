package cn.com.goldwind.md4x.business.service.zeppelin;

import com.amazonaws.util.StringUtils;
import com.google.common.base.Preconditions;

final class ZeppelinInstanceFlavor {
    public final static class Flavor {
        public final String cpu;
        public final String memory;

        private Flavor(String cpu, String memory) {
            Preconditions.checkState(!StringUtils.isNullOrEmpty(cpu));
            Preconditions.checkState(!StringUtils.isNullOrEmpty(memory));

            this.cpu = cpu;
            this.memory = memory;
        }
    }

    public static final Flavor C1M4 = new Flavor("1024", "4096");//1核 CPU, 4GB内存
    public static final Flavor C2M8 = new Flavor("2048", "8192");//2核 CPU, 8GB内存
    public static final Flavor C4M24 = new Flavor("4096", "24576");//4核 CPU, 24GB内存
    public static final Flavor C10M24 = new Flavor("10240", "24576");//4核 CPU, 24GB内存
}

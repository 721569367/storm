package com.alipay.dw.jstorm.schedule;

import java.util.List;

import org.apache.log4j.Logger;

import com.alipay.dw.jstorm.cluster.StormClusterState;
import com.alipay.dw.jstorm.daemon.nimbus.NimbusData;
import com.alipay.dw.jstorm.daemon.nimbus.NimbusUtils;
import com.alipay.dw.jstorm.daemon.nimbus.StatusType;

/**
 * 
 * Scan all task's heartbeat,
 * if task isn't alive, DO NimbusUtils.transition(monitor)
 * 
 * @author Longda
 * 
 */
public class MonitorRunnable implements Runnable {
    private static Logger LOG = Logger.getLogger(MonitorRunnable.class);
    
    private NimbusData    data;
    
    public MonitorRunnable(NimbusData data) {
        this.data = data;
    }
    
    @Override
    public void run() {
        StormClusterState clusterState = data.getStormClusterState();
        
        try {
            // Attetion, here don't check /ZK-dir/taskbeats to 
            // get active topology list
            List<String> active_topologys = clusterState.assignments(null);
            
            if (active_topologys == null) {
                LOG.info("Failed to get active topologies");
                return;
            }
            
            for (String topologyid : active_topologys) {
                
                LOG.debug("Check tasks " + topologyid);
                
                // Attention, here don't check /ZK-dir/taskbeats/topologyid to
                // get task ids
                List<Integer> taskIds = clusterState.task_ids(topologyid);
                if (taskIds == null) {
                    LOG.info("Failed to get task ids of " + topologyid);
                    continue;
                }
                
                boolean needReassign = false;
                for (Integer task : taskIds) {
                    boolean isTaskDead = NimbusUtils.isTaskDead(data,
                            topologyid, task);
                    if (isTaskDead == true) {
                        LOG.info("Found " + topologyid + ",taskid:" + task + " is dead");
                        needReassign = true;
                        break;
                    }
                }
                if (needReassign == true) {
                    NimbusUtils.transition(data, topologyid, false,
                            StatusType.monitor);
                }
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error(e.getCause(), e);
        }
    }
    
}

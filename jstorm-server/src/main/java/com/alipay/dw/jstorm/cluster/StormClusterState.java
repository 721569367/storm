package com.alipay.dw.jstorm.cluster;

import java.util.List;

import com.alipay.dw.jstorm.callback.RunnableCallback;
import com.alipay.dw.jstorm.daemon.supervisor.SupervisorInfo;
import com.alipay.dw.jstorm.task.Assignment;
import com.alipay.dw.jstorm.task.TaskInfo;
import com.alipay.dw.jstorm.task.error.TaskError;
import com.alipay.dw.jstorm.task.heartbeat.TaskHeartbeat;

/**
 * all storm in zk operation interface
 */
public interface StormClusterState {
    public List<String> assignments(RunnableCallback callback) throws Exception;
    
    public Assignment assignment_info(String storm_id, RunnableCallback callback)
            throws Exception;
    
    public void set_assignment(String stormId, Assignment info)
            throws Exception;
    
    public List<String> active_storms() throws Exception;
    
    public StormBase storm_base(String storm_id, RunnableCallback callback)
            throws Exception;
    
    public void activate_storm(String storm_id, StormBase storm_base)
            throws Exception;
    
    public void update_storm(String storm_id, StormStatus new_elems)
            throws Exception;
    
    public void remove_storm_base(String storm_id) throws Exception;
    
    public void remove_storm(String storm_id) throws Exception;
    
    public List<Integer> task_ids(String stromId) throws Exception;
    
    public void set_task(String storm_id, int task_id, TaskInfo info)
            throws Exception;
    
    public TaskInfo task_info(String storm_id, int task_id) throws Exception;
    
    public List<String> task_storms() throws Exception;
    
    public void setup_heartbeats(String storm_id) throws Exception;
    
    public void teardown_heartbeats(String storm_id) throws Exception;
    
    public List<String> heartbeat_storms() throws Exception;
    
    public List<String> heartbeat_tasks(String storm_id) throws Exception;
    
    public TaskHeartbeat task_heartbeat(String stormId, int taskId)
            throws Exception;
    
    public void task_heartbeat(String stormId, int taskId, TaskHeartbeat info)
            throws Exception;
    
    public void remove_task_heartbeat(String storm_id, int task_id)
            throws Exception;
    
    public List<String> supervisors(RunnableCallback callback) throws Exception;
    
    public SupervisorInfo supervisor_info(String supervisor_id)
            throws Exception;
    
    public void supervisor_heartbeat(String supervisor_id, SupervisorInfo info)
            throws Exception;
    
    public void teardown_task_errors(String storm_id) throws Exception;
    
    public List<String> task_error_storms() throws Exception;
    
    public void report_task_error(String storm_id, int task_id, Throwable error)
            throws Exception;
    
    public List<TaskError> task_errors(String storm_id, int task_id)
            throws Exception;
    
    public void disconnect() throws Exception;
}

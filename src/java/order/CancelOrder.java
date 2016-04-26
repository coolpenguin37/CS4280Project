/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import order.*;
/**
 *
 * @author yanlind
 */
public class CancelOrder implements Job {
    public int orderID=-1;
    public CancelOrder(){
        
    }
    public void execute(JobExecutionContext context) throws JobExecutionException{
        JobDataMap data=context.getJobDetail().getJobDataMap();
        int orderID=data.getInt("orderID");
        if (Order.getOrderByOrderID(orderID).getStatus()==1){
            Order.updateStatus(orderID, OrderStatus.ABORTED);
        }
        
    }
            
}



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

/**
 *
 * @author Lin Jianxiong
 */
public interface OrderStatus {
    
    static final int PROCESSING = 1;
    static final int ONGOING = 2;
    static final int STAYING = 3;
    static final int COMPLETED = 4;
    static final int HOLDING = 5;
    static final int ABORTED = 6;
    
}

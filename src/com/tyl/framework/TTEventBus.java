package com.tyl.framework;

import com.tyl.framework.event.EventBus;

/**
 * 1.Event：事件,可以是任意类型的对象。
 * 
 * 2.Subscriber：事件订阅者，接收特定的事件
 * 在EventBus中，使用约定来指定事件订阅者以简化使用。即所有事件订阅都都是以onEvent开头的函数，具体来说，
 * 函数的名字是onEvent，onEventMainThread，onEventBackgroundThread，onEventAsync这四个，这个和ThreadMode有关
 * 
 * 3.Publisher:事件发布者，用于通知Subscriber有事件发生
 * 可以在任意线程任意位置发送事件，直接调用EventBus的`post(Object)`方法
 * 
 * Using EventBus takes four simple steps:
 * 1.Implement any number of event handling methods in the subscriber:
 * public void onEvent(AnyEventType event) {}
 * 
 * 2.Register subscribers:
 * FTEventBus.register(this);
 * 
 * 3.Post events to the bus:
 * FTEventBus.post(event);
 * 
 * 4.Unregister subscriber:
 * FTEventBus.unregister(this);
 * @author LaoYing
 *
 */
public class TTEventBus {

	private static EventBus eventBusInstance;
	
	static{
		eventBusInstance=EventBus.getDefault();
	}
	
	/**
	 * 注册订阅者
	 * @param subscriber 订阅者
	 */
	public static void register(Object subscriber) {
		eventBusInstance.register(subscriber);
    }
	
	/**
	 * 注册订阅者
	 * @param subscriber 订阅者
	 * @param priority 优先级别  默认为0
	 */
	public static void register(Object subscriber, int priority) {
		eventBusInstance.register(subscriber, priority);
    }
	
	/**
	 * 当通过`postSticky`发送一个事件时，这个类型的事件的最后一次事件会被缓存起来，当有订阅者通过`registerSticky`注册时，会把之前缓存起来的这个事件直接发送给它。
	 * @param subscriber
	 */
	public static void registerSticky(Object subscriber) {
		eventBusInstance.registerSticky(subscriber);
    }
	
	/**
	 * 当通过`postSticky`发送一个事件时，这个类型的事件的最后一次事件会被缓存起来，当有订阅者通过`registerSticky`注册时，会把之前缓存起来的这个事件直接发送给它。
	 * @param subscriber
	 * @param priority
	 */
	 public static void registerSticky(Object subscriber, int priority) {
		 eventBusInstance.registerSticky(subscriber, priority);
	 }
	 
	 /**
	  * 判断订阅者是否已经订阅了
	  * @param subscriber
	  * @return
	  */
	 public static boolean isRegistered(Object subscriber) {
	        return eventBusInstance.isRegistered(subscriber);
	 }
	 
	 /**
	  * 订阅者注销
	  * @param subscriber
	  */
	 public static void unregister(Object subscriber) {
		 eventBusInstance.unregister(subscriber);
	 }
	 
	 /**
	  * 发送事件
	  * @param event
	  */
	 public static void post(Object event) {
		 eventBusInstance.post(event);
	 }
	 
	 /**
	  * 取消正在投递的事件
	  * @param event
	  */
	 public static void cancelEventDelivery(Object event) {
		 eventBusInstance.cancelEventDelivery(event); 
	 }
	 
	 /**
	  * 发送缓冲事件,事件会缓冲起来
	  * @param event
	  */
	 public static void postSticky(Object event) {
		 eventBusInstance.postSticky(event); 
	 }
	 
	 /**
	  * 获得接受缓冲事件的事件订阅者
	  * @param eventType
	  * @return
	  */
	 public static <T> T getStickyEvent(Class<T> eventType) {
		 return eventBusInstance.getStickyEvent(eventType);
	 }
	 
	 /**
	  *  移除接受缓冲事件的事件订阅者
	  * @param eventType
	  * @return
	  */
	 public static <T> T removeStickyEvent(Class<T> eventType) {
		 return eventBusInstance.removeStickyEvent(eventType);
	 }
	 
	 /**
	  *  移除接受缓冲事件的事件订阅者
	  * @param event
	  * @return
	  */
	 public static boolean removeStickyEvent(Object event) {
		 return eventBusInstance.removeStickyEvent(event);
	 }
	 
	 /**
	  * 移除所有接受缓冲事件的事件订阅者
	  */
	 public static void removeAllStickyEvents() {
		 eventBusInstance.removeAllStickyEvents();
	 }
	 
	 /**
	  * 判断某个事件是否订阅
	  * @param eventClass
	  * @return
	  */
	 public static boolean hasSubscriberForEvent(Class<?> eventClass) {
		 return eventBusInstance.hasSubscriberForEvent(eventClass);
	 }
	 
}

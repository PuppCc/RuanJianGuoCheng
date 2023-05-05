package com.easyse.easyse_simple.utils;

import org.quartz.SimpleTrigger;

import java.util.*;

/**
 * @author: zky
 * @date: 2023/01/09
 * @description: LRU缓存
 */
public class LRU {

    // 创建双向链表
    static class Node {
        String value;
        Node pre;
        Node next;
        public Node(String value) {
            this.value = value;
            pre = null;
            next = null;
        }
    }
    private Map<String, Node> map = new HashMap();
    private Node head = new Node("");
    private Node tail = new Node("");
    private int size = 10; // 记录长度

    public LRU(int capacity) {
        // write code here
        this.size = capacity;
        // 必须得赋值
        head.next = tail;
        tail.pre = head;
    }

    public String get(String key) {
        // write code here
        // 查询到数据后，需要更新缓存
        String res = "";
        if(map.containsKey(key)) {
            Node node = map.get(key);
            res = node.value;
            removeTOHead(node);
        }
        return res;
    }

    public void set(String key) {
        // write code here
        if (!map.containsKey(key)) {
            Node node = new Node(key);
            map.put(key, node);
            // 如果还有剩余
            if (size > 0) {
                size--;
                // 插入到表头
                insertToHead(node);
            } else {
                // 从双向链表中移除
                removeFromTail();
                insertToHead(node);
            }
        } else {
            // 覆盖之前的元素
            map.get(key).value = key;
            //访问过后，移到表头
            removeTOHead(map.get(key));
        }
    }

    public void insertToHead(Node node) {
        node.next = head.next;
        node.pre = head;
        head.next.pre = node;
        // 这里出错了， node
        head.next = node;
    }

    public void removeFromTail() {
        // hash表中去处节点
        map.remove(tail.pre);
        tail.pre.pre.next = tail;
        tail.pre = tail.pre.pre;
    }

    /**
     * 移动到链表头
     *
     */
    public void removeTOHead(Node node) {
        if(node == head.next) {
            return ;
        }
        // 在链表中删除原始节点的位置
        node.pre.next = node.next;
        node.next.pre = node.pre;
        // 移动到头节点
        insertToHead(node);
    }


    /**
     * 获取最近搜索的数据
     */
    public List<String> getHobby(){
        Node cur = head.next;
        List<String> res = new LinkedList<>();
        while(!Objects.isNull(cur)){
            res.add(cur.value);
            cur = cur.next;
        }
        return res;
    }
}

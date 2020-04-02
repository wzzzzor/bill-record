package com.wzzzzor.billrecord.utils;

import com.wzzzzor.billrecord.domain.HeroNode;

public class SingleLinkedList {

	public HeroNode head = new HeroNode(0, "", "");
	
	public void add(HeroNode node) {
		HeroNode temp = head;
		//编号是否存在
		boolean flag = false;
		while(true) {
			if(temp.next == null) {
				break;
			}
			if(temp.next.no > node.no) {
				break;
			}
			if(temp.next.no == node.no) {
				flag = true;
				break;
			}
			temp = temp.next;
		}
		
		if(flag) {
			throw new RuntimeException("已存在该名次的英雄");
		}else {
			node.next = temp.next;
			temp.next = node;
			
		}
		
	}
	
	public void update(HeroNode node) {
		HeroNode temp = head.next;
		
		//是否存在该编号
		boolean flag = false;
		while(true) {
			if(temp == null) {//遍历完毕
				break;
			}
			if(temp.no == node.no) {
				flag = true;
				break;
			}
			temp = temp.next;
		}
		
		if(flag) {
			temp.name = node.name;
			temp.nickname = node.nickname;
		}else {
			throw new RuntimeException("没有相应的英雄");
		}
	}
	
	public void delete(int no) {
		HeroNode temp = head;
		boolean flag = false;
		while(true) {
			if(temp.next == null) {
				break;
			}
			if(temp.next.no == no) {
				flag = true;
				break;
				//temp.next = temp.next.next;
			}
			temp = temp.next;
		}
		if(flag) {
			temp.next = temp.next.next;
		}else {
			throw new RuntimeException("编号不存在");
		}
	}
	
	public void list() {
		HeroNode temp = head.next;
		if(temp == null) {
			System.out.println("链表为空!");
		}
		while(true) {
			if(temp == null) {//遍历完毕
				break;
			}
			System.out.println(temp);
			temp = temp.next;
		}
	}
	
	
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[");
		HeroNode temp = head.next;
		while(true) {
			if(temp == null) {
				stringBuffer.append("");
				break;
			}else {
				if(temp.next == null) {
					stringBuffer.append(temp.toString());
				}else {
					stringBuffer.append(temp.toString()+",");
				}
			}
			temp = temp.next;
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}

	public static void main(String[] args) {
		SingleLinkedList list = new SingleLinkedList();
		
		list.add(new HeroNode(2, "ljy", "yql"));
		list.add(new HeroNode(3, "wy", "zdx"));
		list.add(new HeroNode(4, "lc", "bzt"));
		list.add(new HeroNode(1, "sj", "jsy"));
		
		
		
		/*list.list();
		
		System.out.println("修改一把");
		list.update(new HeroNode(2, "卢俊义", "玉麒麟"));
		list.list();
		
		System.out.println("删除一把");
		list.delete(1);
		list.list();
		System.out.println("删除一把");
		list.delete(4);
		list.list();*/
		System.out.println(list.toString());
	}
}

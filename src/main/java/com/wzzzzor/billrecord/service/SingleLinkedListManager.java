package com.wzzzzor.billrecord.service;

import java.util.List;

import com.wzzzzor.billrecord.domain.HeroNode;

public interface SingleLinkedListManager {

	/**
	 * 向单链表添加一个节点
	 * @param node
	 */
	public void add(HeroNode node);
	
	/**
	 * 获取单链表所有节点
	 * @return
	 */
	public List<HeroNode> list();
}

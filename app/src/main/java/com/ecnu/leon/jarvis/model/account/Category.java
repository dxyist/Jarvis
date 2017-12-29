package com.ecnu.leon.jarvis.model.account;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.content.Context;

public class Category implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2539518728932669749L;

	private String titleString;
	private boolean isPinned = false;
	private GregorianCalendar createCalendar = null;
	public ArrayList<Category> categories;

	public Category(String titleString)
	{
		this.setCreateCalendar(new GregorianCalendar());
		this.titleString = titleString;
		categories = new ArrayList<>();
	}

	public boolean addSubCategory(Category category)
	{
		// 筛掉名称相等的
		for (int i = 0; i < categories.size(); i++)
		{
			if (category.getTitleString().equalsIgnoreCase(categories.get(i).getTitleString()))
			{
				return false;
			}
		}
		this.categories.add(category);
		return true;
	}

	public boolean removeSubCategory(Category category)
	{
		if (this.categories.size() == 0 || this.categories == null)
			return false;

		for (int i = 0; i < categories.size(); i++)
		{
			if (category.getTitleString().equalsIgnoreCase(categories.get(i).getTitleString()))
			{
				categories.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean cleanSubCategory()
	{
		this.categories.clear();
		return true;
	}

	public String getTitleString()
	{
		return titleString;
	}

	public void setTitleString(String titleString)
	{
		this.titleString = titleString;
	}

	public boolean isPinned()
	{
		return isPinned;
	}

	public void setPinned(boolean isPinned)
	{
		this.isPinned = isPinned;
	}

	public GregorianCalendar getCreateCalendar()
	{
		return createCalendar;
	}

	public void setCreateCalendar(GregorianCalendar createCalendar)
	{
		this.createCalendar = createCalendar;
	}

}

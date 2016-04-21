package com.xlongwei.archetypes.dubbo.util;

import java.io.Serializable;

/**
 * @author hongwei
 * @example
 * <pre>
 * SqlBuilder sql = new SqlBuilder("count(id)", "user");
 * sql.lt("id","10");//添加查询条件
 * int count = TongjiService.count(sql).getObject();
 * sql.select("id,user_name");//查询内容list
 * sql.limit("10");
 * List list = TongjiService.list(sql).getObject();
 * sql.select("sum(id)");//统计sums值
 * sql.limit(null);
 * sql.groupBy("user_name");
 * Object[] sums = TongjiService.sums(sql).getObject();
 * </pre>
 */
@SuppressWarnings("serial")
public class SqlBuilder implements Serializable {
	private String count;
	private String select;
	private String from;
	private StringBuilder where = new StringBuilder();
	private String groupBy;
	private String having;
	private String orderBy;
	private String limit;
	
	public SqlBuilder() {}
	
	/**
	 * @param select
	 * <li>column1,column2,...
	 * <li>count(column1),sum(column2),avg(column3),...
	 * @param from
	 * <li>user
	 * <li>user u left join profile p on u.id = p.user_id
	 */
	public SqlBuilder(String select, String from) {
		if(!isEmpty(select)) this.select = select;
		if(!isEmpty(from)) this.from = from;
	}
	
	/**
	 * @param count *, id, distinct user.id
	 */
	public SqlBuilder count(String count) {
		if(!isEmpty(count)) this.count = count;
		return this;
	}
	
	/**
	 * @param select
	 * <li>column1,column2,...
	 * <li>count(column1),sum(column2),avg(column3),...
	 * <li>user.id,user.name,account.amount,...
	 */
	public SqlBuilder select(String select) {
		if(!isEmpty(select)) this.select = select;
		return this;
	}
	
	/**
	 * @param from
	 * <li>user
	 * <li>user u left join profile p on u.id = p.user_id
	 */
	public SqlBuilder from(String from) {
		if(!isEmpty(from)) this.from = from;
		return this;
	}
	
	/**
	 * @param where
	 * <li>null, to reset where
	 * <li>name like '%Jack%'
	 * <li>exists (select a.id from acount a where a.user_id=user.id)
	 */
	public SqlBuilder where(String where) {
		if(isEmpty(where)) {
			this.where.setLength(0);
		}else {
			this.where.append(" and " + where);
		}
		return this;
	}
	
	/**
	 * @param columnOperator
	 * <li>column1 >=
	 * <li>column2 !=
	 */
	public SqlBuilder where(String columnOperator, String value) {
		if(!isEmpty(value)) {
			where.append(" and " + columnOperator + " '" + sqlParam(value) + "'");
		}
		return this;
	}
	
	public SqlBuilder groupBy(String groupBy) {
		this.groupBy = groupBy;
		return this;
	}
	
	public SqlBuilder having(String having) {
		this.having = having;
		return this;
	}
	
	public SqlBuilder orderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}
	
	/**
	 * @param limit
	 * <li>limit 0,10
	 */
	public SqlBuilder limit(String limit) {
		this.limit = limit;
		return this;
	}
	
	public SqlBuilder eq(String column, String value) {
		if(!isEmpty(value)) {
			where.append(" and " + column + " = '" + sqlParam(value) + "'");
		}
		return this;
	}
	
	public SqlBuilder ne(String column, String value) {
		if(!isEmpty(value)) {
			where.append(" and " + column + " != '" + sqlParam(value) + "'");
		}
		return this;
	}
	
	public SqlBuilder like(String column, String value) {
		if(!isEmpty(value)) {
			where.append(" and " + column + " like '%" + sqlParam(value) + "%'");
		}
		return this;
	}
	
	public SqlBuilder notLike(String column, String value) {
		if(!isEmpty(value)) {
			where.append(" and " + column + " not like '%" + sqlParam(value) + "%'");
		}
		return this;
	}
	
	public SqlBuilder in(String column, String... values) {
		if(!isEmpty(values)) {
			where.append(" and " + column + " in (" + inValuesString(values) + ")");
		}
		return this;
	}
	
	public SqlBuilder notIn(String column, String... values) {
		if(!isEmpty(values)) {
			where.append(" and " + column + " not in (" + inValuesString(values) + ")");
		}
		return this;
	}
	
	public SqlBuilder gt(String column, String value) {
		if(!isEmpty(value)) {
			where.append(" and " + column + " > '" + sqlParam(value) + "'");
		}
		return this;
	}
	
	public SqlBuilder gte(String column, String value) {
		if(!isEmpty(value)) {
			where.append(" and " + column + " >= '" + sqlParam(value) + "'");
		}
		return this;
	}
	
	public SqlBuilder lt(String column, String value) {
		if(!isEmpty(value)) {
			where.append(" and " + column + " < '" + sqlParam(value) + "'");
		}
		return this;
	}
	
	public SqlBuilder lte(String column, String value) {
		if(!isEmpty(value)) {
			where.append(" and " + column + " <= '" + sqlParam(value) + "'");
		}
		return this;
	}
	
	public SqlBuilder between(String column, String from, String to) {
		if(isEmpty(from) && isEmpty(to)) return this;
		if(isEmpty(to)) where.append(" and " + column + " >= '" + sqlParam(from) + "'");
		else if(isEmpty(from)) where.append(" and " + column + " <= '" + sqlParam(to) + "'");
		else where.append(" and " + column + " between '" + sqlParam(from) + "' and '" + sqlParam(to) + "'");
		return this;
	}
	
	public String sql() {
		if(isEmpty(from)) throw new IllegalArgumentException("from must not be empty");
		StringBuilder sql = new StringBuilder("select ");
		if(!isEmpty(select)) sql.append(select);
		else sql.append("*");
		sql.append(" from "+from);
		if(where.length() > 4) sql.append(" where " + where.substring(5));
		if(!isEmpty(groupBy)) sql.append(" group by " + groupBy);
		if(!isEmpty(having)) sql.append(" having " + having);
		if(!isEmpty(orderBy)) sql.append(" order by " + orderBy);
		if(!isEmpty(limit)) sql.append(" limit " + limit);
		return sql.toString();
	}
	
	public String count() {
		if(isEmpty(from)) throw new IllegalArgumentException("from must not be empty");
		StringBuilder sql = new StringBuilder("select ");
		if(!isEmpty(count)) sql.append("count("+count+")");
		else if(!isEmpty(select)) sql.append(select);
		else sql.append("count(*)");
		sql.append(" from "+from);
		if(where.length() > 4) sql.append(" where " + where.substring(5));
		if(!isEmpty(groupBy)) sql.append(" group by " + groupBy);
		if(!isEmpty(having)) sql.append(" having " + having);
		return sql.toString();
	}
	
	public String delete() {
		if(isEmpty(from)) throw new IllegalArgumentException("from must not be empty");
		StringBuilder sql = new StringBuilder("delete from "+from);
		if(where.length() > 4) sql.append(" where " + where.substring(5));
		return sql.toString();
	}
	
	public String update() {
		if(isEmpty(from)) throw new IllegalArgumentException("from must not be empty");
		if(isEmpty(select)) throw new IllegalArgumentException("select must not be empty");
		StringBuilder sql = new StringBuilder("update "+from+" set "+select);
		if(where.length() > 4) sql.append(" where " + where.substring(5));
		return sql.toString();
	}

	public String toString() {
		return sql();
	}

	private static boolean isEmpty(String value) {
    	return value==null || "".equals(value) || value.trim().length()==0;
    }
    private static boolean isEmpty(String[] values) {
    	if(values==null || values.length == 0) return true;
    	for(String value : values) {
    		if(!isEmpty(value)) return false;
    	}
    	return true;
    }
    private static String inValuesString(String[] values) {
    	StringBuilder string = new StringBuilder();
    	for(String value : values) {
    		if(isEmpty(value)) continue;
    		string.append('\'');
    		string.append(value);
    		string.append('\'');
    		string.append(',');
    	}
    	if(string.length()>0) string.deleteCharAt(string.length()-1);
    	return string.toString();
    }
	private static String sqlParam(String sqlParam) {
		return sqlParam.replaceAll("([';]+|(--)+)", "");
	}
}

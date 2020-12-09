package cn.qdzhhl.kbopark.common.data.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.function.Consumer;

public interface KbBaseService<T> extends IService<T> {

	/********************************** 工具模板方法 **********************************/
	default List<T> list(Consumer<QueryWrapper<T>> wrapperConsumer) {
		QueryWrapper<T> query = Wrappers.query();
		if (null != wrapperConsumer) {
			wrapperConsumer.accept(query);
		}
		return list(query);
	}

	default T getOne(Consumer<QueryWrapper<T>> wrapperConsumer) {
		QueryWrapper<T> query = Wrappers.query();
		if (null != wrapperConsumer) {
			wrapperConsumer.accept(query);
		}
		return getOne(query);
	}

	default Integer count(Consumer<QueryWrapper<T>> wrapperConsumer) {
		QueryWrapper<T> query = Wrappers.query();
		if (null != wrapperConsumer) {
			wrapperConsumer.accept(query);
		}
		return count(query);
	}

	default Boolean update(Consumer<UpdateWrapper<T>> wrapperConsumer) {
		UpdateWrapper<T> updateWrapper = Wrappers.update();
		if (null != wrapperConsumer) {
			wrapperConsumer.accept(updateWrapper);
		}
		return update(updateWrapper);
	}

	default Boolean remove(Consumer<QueryWrapper<T>> wrapperConsumer) {
		QueryWrapper<T> query = Wrappers.query();
		if (null != wrapperConsumer) {
			wrapperConsumer.accept(query);
		}
		return remove(query);
	}
}

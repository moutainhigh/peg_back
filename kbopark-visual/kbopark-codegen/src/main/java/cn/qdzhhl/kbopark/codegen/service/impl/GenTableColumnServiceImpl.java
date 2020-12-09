package cn.qdzhhl.kbopark.codegen.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qdzhhl.kbopark.codegen.entity.ColumnEntity;
import cn.qdzhhl.kbopark.codegen.entity.GenConfig;
import cn.qdzhhl.kbopark.codegen.mapper.GenTableColumnMapper;
import cn.qdzhhl.kbopark.codegen.service.GenTableColumnService;
import cn.qdzhhl.kbopark.codegen.util.GenUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 表字段信息管理
 *
 * @author kbopark
 * @date 2020/5/18
 */
@Service
@AllArgsConstructor
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnMapper, ColumnEntity>
		implements GenTableColumnService {

	@Override
	public IPage<ColumnEntity> listTable(Page page, GenConfig genConfig) {
		IPage<ColumnEntity> columnPage = baseMapper.selectTableColumn(page, genConfig.getTableName(),
				genConfig.getDsName());

		// 处理 数据库类型和 Java 类型关系
		Configuration config = GenUtils.getConfig();
		columnPage.getRecords().forEach(column -> {
			String attrType = config.getString(column.getDataType(), "unknowType");
			column.setLowerAttrName(StringUtils.uncapitalize(GenUtils.columnToJava(column.getColumnName())));
			column.setJavaType(attrType);
		});
		return columnPage;
	}

}

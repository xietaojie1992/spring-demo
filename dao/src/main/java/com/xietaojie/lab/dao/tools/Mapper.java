package com.xietaojie.lab.dao.tools;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.RowBoundsMapper;
import tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.delete.DeleteMapper;
import tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper;
import tk.mybatis.mapper.common.base.select.ExistsWithPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.select.SelectAllMapper;
import tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.select.SelectCountMapper;
import tk.mybatis.mapper.common.base.select.SelectMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper;

/**
 * 根据需要，自定义 Mapper
 *
 * @param <T>
 */
public interface Mapper<T> extends

        /** Select
         *
         * SelectOneMapper<T>, SelectOneByExampleMapper<T> // 禁用，用自定义带 limit 的 selectOne 代替原生方法
         * 原生 selectOne 还是 select * from xxx where status=xxx 发往数据库，等拿到结果后，取第一个，造成没必要的数据返回，当数据量大时尤不可取
         */
        SelectMapper<T>, SelectAllMapper<T>, SelectCountMapper<T>, SelectByPrimaryKeyMapper<T>, ExistsWithPrimaryKeyMapper<T>,

        /** Insert
         * Stop using InsertMapper，use InsertSelectiveMapper a
         */
        //InsertMapper<T>, // 尽量使用 InsertSelectiveMapper, 插入时使用数据库默认值
        InsertSelectiveMapper<T>,

        //update
        UpdateByPrimaryKeyMapper<T>, UpdateByPrimaryKeySelectiveMapper<T>,

        //delete
        DeleteMapper<T>, DeleteByPrimaryKeyMapper<T>,

        //example
        ExampleMapper<T>,

        //RowBounds
        RowBoundsMapper<T>,

        //Marker
        Marker {

    /**
     * select ... from ... where ... limit 1
     *
     * @param record
     * @return
     */
    @SelectProvider(type = MapperProvider.class, method = "dynamicSQL")
    T selectOne(T record);

    /**
     * select ... from ... where ... limit 1
     * take place of SelectOneByExampleMapper
     * @param example
     * @return
     */
    @SelectProvider(type = MapperProvider.class, method = "dynamicSQL")
    @Override
    T selectOneByExample(Object example);
}

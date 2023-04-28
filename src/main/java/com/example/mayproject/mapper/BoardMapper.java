package com.example.mayproject.mapper;

import com.example.mayproject.domain.Board;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Select("""
            SELECT 
                id,
                title,
                writer,
                inserted
            FROM board
            ORDER BY id DESC
            """)
    List<Board> selectAll();

    @Select("""
            SELECT *
            FROM Board
            WHERE id = #{id}
            """)
    Board selectById(Integer id);

    @Update("""
            UPDATE board
            SET
                title = #{title},
                body = #{body},
                writer = #{writer}
            WHERE
                id = #{id}
            """)
    int update(Board board);

    @Delete("""
            DELETE
            FROM Board
            WHERE id = #{id}
            """)
    int deleteById(Integer id);

    @Insert("""
            INSERT INTO board (title, body, writer)
            VALUES (#{title}, #{body}, #{writer})
            """)
    boolean createBoard(Board board);

    @Select("""
            SELECT
                id,
                title,
                writer,
                inserted
            FROM Board
            ORDER BY id DESC
            LIMIT #{startIndex}, #{rowPerPage}
            """) // 15개(rowPerPage)씩 게시물 보여주기
    List<Board> selectAllPaging(Integer startIndex, Integer rowPerPage);

    @Select("""
            SELECT COUNT(*)
            FROM board
            """) // 전체 게시물 개수 구하기
    Integer countAll();
}

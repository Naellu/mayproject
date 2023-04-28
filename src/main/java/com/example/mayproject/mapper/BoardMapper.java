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
            <script>
            <bind name="pattern" value="'%' + search + '%'" />
            SELECT
                id,
                title,
                writer,
                inserted
            FROM Board
            WHERE
                title LIKE #{pattern} OR
                body LIKE #{pattern} OR
                writer LIKE #{pattern}
            ORDER BY id DESC
            LIMIT #{startIndex}, #{rowPerPage}
            </script>
            """) // 15개(rowPerPage)씩 게시물 보여주기
    List<Board> selectAllPaging(Integer startIndex, Integer rowPerPage, String search);

    @Select("""
            <script>
            <bind name="pattern" value="'%' + search + '%'" />
            SELECT COUNT(*)
            FROM board
            WHERE
                title LIKE #{pattern} OR
                body LIKE #{pattern} OR
                writer LIKE #{pattern}
            </script>
            """) // 전체 게시물 개수 구하기
    Integer countAll(String search);
}

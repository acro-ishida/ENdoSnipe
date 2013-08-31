/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.data.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.TableNames;
import jp.co.acroquest.endosnipe.data.entity.MultipleResourceGraphDefinition;

/**
 * {@link MultipleResourceGraphDefinition} �̂��߂� DAO �ł��B
 * 
 * @author pin
 *
 */
public class MultipleResourceGraphDefinitionDao extends AbstractDao implements TableNames
{
    /**
     * �w�肳�ꂽ�f�[�^�x�[�X�̃V�O�i����`��S�Ď擾���܂��B<br />
     *
     * �V�O�i����`���o�^����Ă��Ȃ��ꍇ�͋�̃��X�g��Ԃ��܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @return �V�O�i����`�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MultipleResourceGraphDefinition> selectAll(final String database)
        throws SQLException
    {
        List<MultipleResourceGraphDefinition> result = new ArrayList<MultipleResourceGraphDefinition>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, false);
            stmt = conn.createStatement();
            String sql = "select * from " + MULTIPLE_RESOURCE_GRAPH;
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                // CHECKSTYLE:OFF
                MultipleResourceGraphDefinition multipleResourceGraphInfo = new MultipleResourceGraphDefinition();
                multipleResourceGraphInfo.multipleResourceGraphId = rs.getInt(1);
                multipleResourceGraphInfo.multipleResourceGraphName = rs.getString(2);
                multipleResourceGraphInfo.measurementItemIdList = rs.getString(3);
                
                // CHECKSTYLE:ON
                result.add(multipleResourceGraphInfo);
            }
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }

        return result;
    }
    
    /**
     * ���ׂẴ��R�[�h���폜���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void deleteAll(final String database)
        throws SQLException
    {
        deleteAll(database, MULTIPLE_RESOURCE_GRAPH);
    }
}
package pu.porna.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Gekopieerd uit MyBatis
 * 
 * @author Clinton Begin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowBounds
{

public static final int NO_ROW_OFFSET = 0;
public static final int NO_ROW_LIMIT = Integer.MAX_VALUE;

public static final RowBounds DEFAULT = new RowBounds();

private int offset;
private int limit;

}
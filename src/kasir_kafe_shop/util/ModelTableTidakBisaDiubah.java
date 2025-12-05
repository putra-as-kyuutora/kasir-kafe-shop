package kasir_kafe_shop.util;

import javax.swing.table.DefaultTableModel;

public class ModelTableTidakBisaDiubah extends DefaultTableModel {
    
    public ModelTableTidakBisaDiubah(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}

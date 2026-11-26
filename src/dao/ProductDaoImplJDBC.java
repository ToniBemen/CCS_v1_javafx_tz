package dao;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.PreparedStatement;

import model.Product;

public class ProductDaoImplJDBC implements ProductDao {
	private static final String DEFAULT_DB_URL = "jdbc:h2:./data/computercenter_v1_starter;AUTO_SERVER=FALSE";
	private static final String DEFAULT_USER = "sa";
	private static final String DEFAULT_PASSWORD = "";

	private Connection conn;

	@Override
	public void connect() throws SQLException {
		String url = System.getProperty("computercenter.db.url", DEFAULT_DB_URL);
		String user = System.getProperty("computercenter.db.user", DEFAULT_USER);
		String password = System.getProperty("computercenter.db.password", DEFAULT_PASSWORD);

		conn = DriverManager.getConnection(url, user, password);
		
	}

	@Override
	public void disconnect() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.close();
		}
	}


	// TODO RA2-ISSUE-03: crear products, inventory_history i l'alias STOCK_ALERT_LIMIT.
			// Pista: resol-ho amb Statement i try-with-resources, i afegeix seed inicial si la taula es buida.
	@Override
	public void initSchema() throws SQLException {
		Connection connection = requireConnection();

		String createProductsSql = """
				CREATE TABLE IF NOT EXISTS products (
					id INT AUTO_INCREMENT PRIMARY KEY,
					name VARCHAR(120) NOT NULL UNIQUE,
					wholesaler_price DECIMAL(10, 2) NOT NULL,
					available BOOLEAN NOT NULL DEFAULT TRUE,
					stock INT NOT NULL DEFAULT 0
				)//
				""";

		String createInventoryHistorySql = """
				CREATE TABLE IF NOT EXISTS inventory_history (
					id INT AUTO_INCREMENT PRIMARY KEY,
					product_id INT NOT NULL,
					change_amount INT NOT NULL,
					reason VARCHAR(255),
					created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
					CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products(id)
				)
				""";

		String createStockAlertAliasSql = """
				CREATE ALIAS IF NOT EXISTS STOCK_ALERT_LIMIT FOR "dao.ProductDaoImplJDBC.stockAlertLimit"
				""";

		try (Statement stmt = connection.createStatement()) {
			stmt.execute(createProductsSql);
			stmt.execute(createInventoryHistorySql);
			stmt.execute(createStockAlertAliasSql);

			try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products")) {
				if (rs.next() && rs.getInt(1) == 0) {
					stmt.executeUpdate("""
							INSERT INTO products (name, wholesaler_price, available, stock) VALUES
								('Laptop Ryzen 5', 520.00, TRUE, 10),
								('Mechanical Keyboard', 45.00, TRUE, 3),
								('USB-C Hub 7-in-1', 22.50, TRUE, 15)
							""");
				}
			}
		}
	}

	@Override
	public ArrayList<Product> getInventory() throws SQLException {
		// TODO RA2-ISSUE-07: executar SELECT ordenat i convertir cada fila en Product amb mapProduct().
		throw pending("RA2-ISSUE-07 pendent: implementa consulta d'inventari i recorregut de ResultSet.");
	}

	@Override
	public Product findProductByName(String name) throws SQLException {
		// TODO RA2-ISSUE-07: buscar per nom amb PreparedStatement i retornar null si no hi ha resultat.
		throw pending("RA2-ISSUE-07 pendent: implementa consulta parametritzada per nom.");
	}

	@Override
	public int insertProduct(Product product) throws SQLException {
		String sql = "INSERT INTO products (name, wholesaler_price, available, stock) VALUES (?, ?, ?, ?)";
		
		try (PreparedStatement pstmt = requireConnection().prepareStatement(sql)) {
			pstmt.setString(1,  product.getName());
			pstmt.setDouble(2,  product.getWholesalerPrice().getValue());
			pstmt.setBoolean(3,  product.isAvailable());
			pstmt.setInt(4,  product.getStock());
			
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int updateStock(String name, int newStock) throws SQLException {
		String sql = "UPDATE products SET stock = ?, available = ?, WHERE name = ?";
		
		try (PreparedStatement pstmt = requireConnection().prepareStatement(sql)) {
			pstmt.setInt(1, newStock);
			pstmt.setBoolean(2, newStock > 0);
			pstmt.setString(3,  name);
			
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int deleteProduct(String name) throws SQLException {
		// TODO RA2-ISSUE-06: eliminar per nom i comprovar files afectades.
		throw pending("RA2-ISSUE-06 pendent: implementa eliminacio controlada.");
	}

	@Override
	public void registerStockChange(String name, int unitsToAdd) throws SQLException {
		// TODO RA2-ISSUE-09: desactivar autoCommit, actualitzar estoc, inserir historial, commit o rollback.
		throw pending("RA2-ISSUE-09 pendent: implementa transaccio commit/rollback.");
	}

	@Override
	public boolean evaluateStockAlertWithProcedure(int stock) throws SQLException {
		// TODO RA2-ISSUE-10: cridar STOCK_ALERT_LIMIT amb CallableStatement i parametre d'entrada.
		throw pending("RA2-ISSUE-10 pendent: implementa procediment o alias amb CallableStatement.");
	}

	public static int stockAlertLimit(int stock) {
		return stock < 5 ? 1 : 0;
	}

	private Product mapProduct(ResultSet resultSet) throws SQLException {
		// TODO RA2-ISSUE-07: crear Product amb name, wholesaler_price, available, stock i assignar id.
		throw pending("RA2-ISSUE-07 pendent: implementa mapatge de ResultSet a Product.");
	}

	private SQLFeatureNotSupportedException pending(String message) {
		return new SQLFeatureNotSupportedException(message);
	}

	@SuppressWarnings("unused")
	private Connection requireConnection() throws SQLException {
		if (conn == null || conn.isClosed()) {
			throw new SQLException("Connexio no disponible. Revisa RA2-ISSUE-02.");
		}
		return conn;
	}
}

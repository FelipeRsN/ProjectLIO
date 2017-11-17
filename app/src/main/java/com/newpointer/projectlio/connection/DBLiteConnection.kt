package com.newpointer.projectlio.connection

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.newpointer.projectlio.model.*
import java.util.*


@Suppress("UNREACHABLE_CODE")
/**
 * Created by Felipe Silveira on 10/5/2017.
 */
class DBLiteConnection private constructor(ctx: Context) {
    private var db: SQLiteDatabase

    companion object {
        private var instance: DBLiteConnection? = null

        fun getInstance(ctx: Context): DBLiteConnection {
            if (instance == null) instance = DBLiteConnection(ctx)
            return instance as DBLiteConnection
        }
    }

    init {
        val dbcore = DBCore.getInstance(ctx)
        db = dbcore.writableDatabase
    }

    /////////////////////// Configuracoes /////////////////////////
    ////// insert /////////

    fun insertConfig(string_bd: String, estacao: String, taxa: Double?, digito_verificador: Int, pergunta_mesa: Int, titulo_loja: String, nmin_mesa: String, nmax_mesa: String, bdbkp_date: String, phone_selec: Int, product_selec: Int, preconta: Int, conferencia: Int, lio: Int) {
        val values = ContentValues()
        values.put("db_string", string_bd)
        values.put("estacao", estacao)
        values.put("taxa", taxa)
        values.put("digito_verificador", digito_verificador)
        values.put("pergunta_mesa", pergunta_mesa)
        values.put("titulo_loja", titulo_loja)
        values.put("nmin_mesa", nmin_mesa)
        values.put("nmax_mesa", nmax_mesa)
        values.put("dbbkp_date", bdbkp_date)
        values.put("phone_selection", phone_selec)
        values.put("product_selection", product_selec)
        values.put("preconta", preconta)
        values.put("conferencia", conferencia)
        values.put("lio", lio)
        db.insert("config", null, values)
    }

    ////// Delete /////////

    fun deleteConfig() {
        db.delete("config", null, null)
    }

    ////// Select /////////

    @SuppressLint("Recycle")
    fun selectConfig(): ConfigModel? {
        val columns = arrayOf("db_string", "estacao", "taxa", "digito_verificador", "pergunta_mesa", "titulo_loja", "nmin_mesa", "nmax_mesa", "dbbkp_date", "phone_selection", "product_selection", "preconta", "conferencia", "lio")
        val cursor = db.query("config", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                return ConfigModel(cursor.getString(0), cursor.getString(1), cursor.getDouble(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), cursor.getInt(12), cursor.getInt(13))
            } while (cursor.moveToNext())
        }
        return null
    }

    @SuppressLint("Recycle")
    fun isConfigurated(): Boolean {
        val columns = arrayOf("db_string")
        val cursor = db.query("config", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                return true
            } while (cursor.moveToNext())
        }
        return false
    }

    /////////////////////// Produto /////////////////////////
    ////// insert /////////

    fun insertProd(id: String?, name: String?, fam: Int?, unit: String?, fl_imp: Int?, cd_imp: Int?, fl_acomp: Int?, acomp: String?, atalho: Int?) {
        val values = ContentValues()
        values.put("id", id)
        values.put("name", name)
        values.put("family", fam)
        values.put("unit", unit)
        values.put("fl_imp", fl_imp)
        values.put("cd_imp", cd_imp)
        values.put("fl_acomp", fl_acomp)
        values.put("acomp", acomp)
        values.put("atalho", atalho)
        db.insert("product", null, values)
    }

    ////// Delete /////////

    fun deleteAllProd() {
        db.delete("product", null, null)
    }

    ////// Select /////////

    @SuppressLint("Recycle")
    fun selectAllProd(): ArrayList<ProductModel> {
        val prod = ArrayList<ProductModel>()
        val columns = arrayOf("id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp")
        val cursor = db.query("product", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                prod.add(ProductModel(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(7)))
            } while (cursor.moveToNext())
        }
        return prod
    }

    fun haveProd(): Boolean {
        val columns = arrayOf("id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp")
        val cursor = db.query("product", columns, null, null, null, null, null)
        return cursor.moveToFirst()
    }

    @SuppressLint("Recycle")
    fun selectProdByFam(codefam: Int): ArrayList<ProductModel> {
        val prod = ArrayList<ProductModel>()
        val columns = arrayOf("id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp")
        val cursor = db.query("product", columns, "family = " + codefam, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                prod.add(ProductModel(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(7)))
            } while (cursor.moveToNext())
        }
        return prod
    }

    @SuppressLint("Recycle")
    fun selectProdByName(name: String): ArrayList<ProductModel> {
        val prod = ArrayList<ProductModel>()
        val columns = arrayOf("id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp")
        val cursor = db.query("product", columns, "name LIKE '%$name%'", null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                prod.add(ProductModel(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(7)))
            } while (cursor.moveToNext())
        }
        return prod
    }

    @SuppressLint("Recycle")
    fun foundSelectProdByName(name: String): Boolean {
        val columns = arrayOf("id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp")
        val cursor = db.query("product", columns, "name LIKE '%$name%'", null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                return true
            } while (cursor.moveToNext())
        }
        return false
    }

    @SuppressLint("Recycle")
    fun foundSelectProdAtalhos(): Boolean {
        val columns = arrayOf("id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp")
        val cursor = db.query("product", columns, "atalho = 1", null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                return true
            } while (cursor.moveToNext())
        }
        return false
    }

    @SuppressLint("Recycle")
    fun selectProdByAtalho(): ArrayList<ProductModel> {
        val prod = ArrayList<ProductModel>()
        val columns = arrayOf("id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp")
        val cursor = db.query("product", columns, "atalho = 1", null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                prod.add(ProductModel(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(7)))
            } while (cursor.moveToNext())
        }
        return prod
    }

    @SuppressLint("Recycle")
    fun getProdByCode(code: String): ProductModel {
        val prod = ProductModel("0", "", 0, "", 0, 0, 0, "")
        val columns = arrayOf("id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp")
        val cursor = db.query("product", columns, "id = '$code'", null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                prod.id = cursor.getString(0)
                prod.name = cursor.getString(1)
                prod.fam = cursor.getInt(2)
                prod.unit = cursor.getString(3)
                prod.fl_imp = cursor.getInt(4)
                prod.cd_imp = cursor.getInt(5)
                prod.fl_acomp = cursor.getInt(6)
                prod.acomp = cursor.getString(7)
            } while (cursor.moveToNext())
        }
        return prod
    }

    /////////////////////// Familia  /////////////////////////
    ////// insert /////////

    fun insertFam(id: Int, name: String) {
        val values = ContentValues()
        values.put("id", id)
        values.put("name", name)
        db.insert("family", null, values)
    }

    ////// Delete /////////

    fun deleteAllFam() {
        db.delete("family", null, null)
    }

    ////// Select /////////

    @SuppressLint("Recycle")
    fun selectAllFam(): ArrayList<FamilyModel> {
        val fam = ArrayList<FamilyModel>()
        val columns = arrayOf("id", "name")
        val cursor = db.query("family", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                fam.add(FamilyModel(cursor.getInt(0), cursor.getString(1)))
            } while (cursor.moveToNext())
        }
        return fam
    }

    @SuppressLint("Recycle")
    fun haveFam(): Boolean {
        val fam = ArrayList<FamilyModel>()
        val columns = arrayOf("id", "name")
        val cursor = db.query("family", columns, null, null, null, null, null)
        return cursor.moveToFirst()
    }

    /////////////////////// Acomp  /////////////////////////
    ////// insert /////////

    fun insertAcomp(id: Int, name: String, grupo: Int) {
        val values = ContentValues()
        values.put("id", id)
        values.put("name", name)
        values.put("grupo", grupo)
        db.insert("acomp", null, values)
    }

    ////// Delete /////////

    fun deleteAllAcomp() {
        db.delete("acomp", null, null)
    }

    ////// Select /////////

    @SuppressLint("Recycle")
    fun selectAcompByGroup(group: Int): ArrayList<AcompanhamentoModel> {
        val aco = ArrayList<AcompanhamentoModel>()
        val columns = arrayOf("id", "name", "grupo")
        val cursor = db.query("acomp", columns, "grupo = " + group, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                aco.add(AcompanhamentoModel(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)))
            } while (cursor.moveToNext())
        }
        return aco
    }

    /////////////////////// Grupo Acomp  /////////////////////////
    ////// insert /////////

    fun insertGrupoAcomp(id: Int, name: String, selecao: Int) {
        val values = ContentValues()
        values.put("id", id)
        values.put("name", name)
        values.put("selecao", selecao)
        db.insert("groupacomp", null, values)
    }

    ////// Delete /////////

    fun deleteAllGrupoAcomp() {
        db.delete("groupacomp", null, null)
    }

    ////// Select /////////

    @SuppressLint("Recycle")
    fun selectGrupoAcomp(id: Int): GrupoAcompModel {
        val group = GrupoAcompModel(0, "", 0)
        val columns = arrayOf("id", "name", "selecao")
        val cursor = db.query("groupacomp", columns, "id = " + id, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                group.id = cursor.getInt(0)
                group.name = cursor.getString(1)
                group.selecao = cursor.getInt(2)
            } while (cursor.moveToNext())
        }
        return group
    }

    /////////////////////// Carrinho /////////////////////////
    ////// insert /////////

    fun insertProdCarrinho(id: String, name: String, qtd: Int, acomp: String, obs: String) {
        val values = ContentValues()
        values.put("id_prod", id)
        values.put("name_prod", name)
        values.put("qtd_prod", qtd)
        values.put("acomp_prod", acomp)
        values.put("obs_prod", obs)
        db.insert("carrinho", null, values)
    }

    ////// Delete /////////

    fun deleteAllCarrinho() {
        db.delete("carrinho", null, null)
    }

    fun deleteProdCarrinho(id: Int) {
        db.delete("carrinho", "id_carrinho = " + id, null)
    }

    ////// Select /////////

    @SuppressLint("Recycle")
    fun selectCarrinho(): ArrayList<CarrinhoModel> {
        val carrinho = ArrayList<CarrinhoModel>()
        val columns = arrayOf("id_carrinho", "id_prod", "name_prod", "qtd_prod", "acomp_prod", "obs_prod")
        val cursor = db.query("carrinho", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                carrinho.add(CarrinhoModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5)))
            } while (cursor.moveToNext())
        }
        return carrinho
    }

    @SuppressLint("Recycle")
    fun haveProdInCarrinho(): Boolean {
        val columns = arrayOf("id_carrinho")
        val cursor = db.query("carrinho", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                return true
            } while (cursor.moveToNext())
        }
        return false
    }

    /////////////////////// Operador /////////////////////////
    ////// insert /////////

    fun insertOp(id: Int, name: String, fliniciar: Int, flprimeiro: Int, pass: String, flperfil: Int, cdperfil: Int) {
        val values = ContentValues()
        values.put("id_usu", id)
        values.put("name", name)
        values.put("fl_iniciar", fliniciar)
        values.put("fl_primeiro_acesso", flprimeiro)
        values.put("password", pass)
        values.put("fl_perfil", flperfil)
        values.put("cd_perfil", cdperfil)
        db.insert("operador", null, values)
    }

    fun insertOpFunc(cd: Int, func: String) {
        val values = ContentValues()
        values.put("cd_operador", cd)
        values.put("cd_funcao", func)
        db.insert("operador_funcao", null, values)
    }

    fun insertPerfilFuncao(cd: Int, func: String) {
        val values = ContentValues()
        values.put("cd_perfil", cd)
        values.put("cd_funcao", func)
        db.insert("perfil_funcao", null, values)
    }

    ////// Delete /////////

    fun deleteAllOP() {
        db.delete("operador", null, null)
    }

    fun deleteAllOPFunc() {
        db.delete("operador_funcao", null, null)
    }

    fun deleteAllPerfilFunc() {
        db.delete("perfil_funcao", null, null)
    }

    ////// Select /////////

    @SuppressLint("Recycle")
    fun selectOp(cd: Int): OperadorModel? {
        val op: OperadorModel
        val columns = arrayOf("id_usu", "name", "fl_iniciar", "fl_primeiro_acesso", "password", "fl_perfil", "cd_perfil")
        val cursor = db.query("operador", columns, "id_usu = " + cd, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                op = OperadorModel(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6))
                return op
            } while (cursor.moveToNext())
        }
        return null
    }

    @SuppressLint("Recycle")
    fun selectOpFunc(cd: Int): String {
        var result = ""
        val columns = arrayOf("cd_funcao")
        val cursor = db.query("operador_funcao", columns, "cd_operador = " + cd, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                result = cursor.getString(0)
            } while (cursor.moveToNext())
        }
        return result
    }

    @SuppressLint("Recycle")
    fun selectPerfilFunc(cd: Int): String {
        var result = ""
        val columns = arrayOf("cd_funcao")
        val cursor = db.query("perfil_funcao", columns, "cd_perfil = " + cd, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                result = cursor.getString(0)
            } while (cursor.moveToNext())
        }
        return result
    }

    /////////////////////// Current_Operador /////////////////////////
    ////// insert /////////

    fun insertCurrentOp(id: Int, name: String) {
        val values = ContentValues()
        values.put("cd_operador", id)
        values.put("nm_operador", name)
        db.insert("current_op", null, values)
    }

    ////// Delete /////////

    fun deleteCurrentOP() {
        db.delete("current_op", null, null)
    }


    ////// Select /////////

    @SuppressLint("Recycle")
    fun selectCurrentOp(): CurrentOperadorModel? {
        val op: CurrentOperadorModel
        val columns = arrayOf("cd_operador", "nm_operador")
        val cursor = db.query("current_op", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                op = CurrentOperadorModel(cursor.getInt(0), cursor.getString(1))
                return op
            } while (cursor.moveToNext())
        }
        return null
    }

    /////////////////////// Associados /////////////////////////
    ////// insert /////////

    fun insertAssociados(id: String, id_prod: String) {
        val values = ContentValues()
        values.put("id_associado", id)
        values.put("id_prod", id_prod)
        db.insert("prod_associado", null, values)
    }

    ////// Delete /////////

    fun deleteAssociados() {
        db.delete("prod_associado", null, null)
    }


    ////// Select /////////

    @SuppressLint("Recycle")
    fun getProdByIdAssociado(id_associado: String): ProductModel {
        val columns = arrayOf("id_prod")
        val cursor = db.query("prod_associado", columns, "id_associado = '$id_associado'", null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                Log.i("GETPRODBYID", "Achou um associado e retornou o codigo de prod " + cursor.getString(0))
                return getProdByCode(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        return ProductModel("0", "", 0, "", 0, 0, 0, "")
    }

    fun getProdByAssociadoAndCode(id: String): ProductModel {
        val p = getProdByCode(id)
        return if (p.getName().equals("", ignoreCase = true)) {
            getProdByIdAssociado(id)
        } else {
            p
        }
    }

    @SuppressLint("Recycle")
    fun getAllAssociados() {
        val columns = arrayOf("id_associado")
        val cursor = db.query("prod_associado", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext())
        }
    }

    /////////////////////// PGTO  /////////////////////////
    ////// insert /////////

    fun insertPgtoToDB(mesa: String, nsu: String, forma_pgto: Int, tipo: String, valor: String) {
        val values = ContentValues()
        values.put("mesa", mesa)
        values.put("nsu", nsu)
        values.put("forma_pgto", forma_pgto)
        values.put("tipo_pgto", tipo)
        values.put("valor", valor)
        db.insert("pgto", null, values)
    }

    ////// Delete /////////

    fun resetPgtoTable() {
        db.delete("pgto", null, null)
    }

    ////// Select /////////

    @SuppressLint("Recycle")
    fun getPgto(mesa: String): ArrayList<HistoricoPagamentoModel> {
        val pgto = ArrayList<HistoricoPagamentoModel>()
        val columns = arrayOf("nsu", "forma_pgto", "tipo_pgto", "valor")
        val cursor = db.query("pgto", columns, "mesa = '$mesa'", null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                pgto.add(HistoricoPagamentoModel(cursor.getString(0),cursor.getString(2),cursor.getInt(1),cursor.getString(3)))
            } while (cursor.moveToNext())
        }
        return pgto
    }

    @SuppressLint("Recycle")
    fun havePaymentOpened(): Boolean {
        val columns = arrayOf("nsu")
        val cursor = db.query("pgto", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                return true
            } while (cursor.moveToNext())
        }
        return false
    }

    @SuppressLint("Recycle")
    fun getMesaWithPaymentOpened(): String {
        val columns = arrayOf("mesa")
        val cursor = db.query("pgto", columns, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                return cursor.getString(0)
            } while (cursor.moveToNext())
        }
        return ""
    }

}
package PluginFiles

import org.apache.commons.io.IOUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.map.MinecraftFont
import org.bukkit.plugin.java.JavaPlugin
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import org.json.simple.parser.ParseException
import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.collections.ArrayList


val main_dir = "plugins/Rylmmorpg"
var MC_VERSION = ""
var command_for_set_item_in_gui = ""
var GUI_TITLE = ""
var GUI_Page = 1
var GUI_TITLE_FOR_FUNCTION = ""
var Message = ""


var Player_DATA_TAGS = YamlConfiguration.loadConfiguration(File(""))
var Player_DATA_1 = YamlConfiguration.loadConfiguration(File(""))
var Player_DATA_2 = YamlConfiguration.loadConfiguration(File(""))
var Player_DATA_3 = YamlConfiguration.loadConfiguration(File(""))

var File_Path_1 = ""
var File_Name_1 = ""
var Yml_1 = YamlConfiguration.loadConfiguration(File(File_Path_1))

var File_Path_2 = ""
var File_Name_2 = ""
var Yml_2 = YamlConfiguration.loadConfiguration(File(File_Path_2))

var File_Path_X = ""
var File_Name_X= ""
var Yml_X = YamlConfiguration.loadConfiguration(File(File_Path_X))

var File_Path_R = ""
var File_Name_R= ""
var Yml_R = YamlConfiguration.loadConfiguration(File(File_Path_R))
//######################################################################################################################
class Main: JavaPlugin(), Listener, CommandExecutor{
//######################################################################################################################
    override fun onLoad() {
        Bukkit.getLogger().info("Plugin loaded!")
        //PacketEvents.getAPI().load()
    }

    override fun onEnable() {

        //PacketEvents.getAPI().init()
        //PacketEvents().registerListener(this)
        Bukkit.getPluginManager().registerEvents(this, this)

        if (!File(main_dir).exists()) {
            File(main_dir).mkdir()
        }

        var NEW_FILE = this.javaClass.classLoader.getResourceAsStream("plugin.yml")!!
        Files.copy(NEW_FILE, File("$main_dir/plugin.yml").toPath(), StandardCopyOption.REPLACE_EXISTING)

        if (!File("$main_dir/Config.yml").exists()) {
            var NEW_FILE = this.javaClass.classLoader.getResourceAsStream("Config.yml")!!
            Files.copy(NEW_FILE, File("$main_dir/Config.yml").toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        if (!File("$main_dir/Chat").exists()) {
            File("$main_dir/Chat").mkdir()
        }
        if (!File("$main_dir/Chat/Global_Chat_REC.yml").exists()) {
            File("$main_dir/Chat/Global_Chat_REC.yml").createNewFile()
        }
        if (!File("$main_dir/Chat/Region.yml").exists()) {
            File("$main_dir/Chat/Region.yml").createNewFile()
        }
        if (!File("$main_dir/Chat/PrivateChat").exists()) {
            File("$main_dir/Chat/PrivateChat").mkdir()
        }
        if (!File("$main_dir/Player_DATA").exists()) {
            File("$main_dir/Player_DATA").mkdir()
        }
        if (!File("$main_dir/UI").exists()) {
            File("$main_dir/UI").mkdir()
        }
        if (!File("$main_dir/Party").exists()) {
            File("$main_dir/Party").mkdir()
        }
        if (!File("$main_dir/Party/Party_DATA").exists()) {
            File("$main_dir/Party/Party_DATA").mkdir()
        }
        if (!File("$main_dir/Party/List_Of_Party_Finder.yml").exists()) {
            File("$main_dir/Party/List_Of_Party_Finder.yml").createNewFile()
        }
        var YML_List_Of_Party_Finder = YamlConfiguration.loadConfiguration(File("$main_dir/Party/List_Of_Party_Finder.yml"))
        if (YML_List_Of_Party_Finder["Party_List"] == null){
            setYmlValue("$main_dir/Party/List_Of_Party_Finder.yml","Party_List",ArrayList<String>())
        }

        if (!File("$main_dir/Guild").exists()) {
            File("$main_dir/Guild").mkdir()
        }

        //==============================================================================================================
        //File_Extract_From_Jar_Or_Zip
        val jar = JarFile(File("$main_dir.jar"))
        val enumEntries: Enumeration<*> = jar.entries()
        while (enumEntries.hasMoreElements()) {
            val file = enumEntries.nextElement() as JarEntry
            val file_from_path = File("$main_dir/" + file.name)

            if (file.name.startsWith("UI")){

                if (file.isDirectory) { // if its a directory, create it
                    file_from_path.mkdir()
                    continue
                }
                val `is`: InputStream = jar.getInputStream(file) // get the input stream
                val fos = FileOutputStream(file_from_path)
                while (`is`.available() > 0) {  // write contents of 'is' to 'fos'
                    fos.write(`is`.read())
                }
                fos.close()
                `is`.close()

            }
        }
        jar.close()
        //==============================================================================================================
    }

    override fun onDisable() {

    }
//######################################################################################################################
//######################################################################################################################
    var Player_In_Event = Bukkit.getPlayer("")
    var Player_UUID = ""
    var CURRENT_RECIPIENT_NAME = ""
    var CURRENT_RECIPIENT_UUID = ""
    var Player_TAGS = ArrayList<String>()
    var TEMP_LIST = ArrayList<String>()
    var TEMP_LIST_X = ArrayList<String>()
    var P_location = Player_In_Event!!.location.clone()
    var menu_gui_1 = Bukkit.createInventory(null, 54, "None")
    var menu_gui_2 = Bukkit.createInventory(null, 54, "None")
    var ITEMSTACK = ItemStack(Material.AIR)
    var ITEMSTACK_mdf = ITEMSTACK.itemMeta
    var Host_total_amount = 5
    var is_Unknown_CMD = false
    var recovery_player_inv_IN_GUI_RUNNING: Array<out ItemStack>? = null
    var record_of_time_when_GUI_close =  ArrayList<Array<String>>()
    var THE_Player_Who_IS_Viewing_Party_Finder =  ArrayList<String>()
//######################################################################################################################
//######################################################################################################################
//Yml_setValue((Sync Support))

    fun setYmlValue(YML_FILE_PATH: String, Key: String, Value: Any?){

        var Yml_XXX = YamlConfiguration.loadConfiguration(File(YML_FILE_PATH))
        Yml_XXX[Key] = Value
        Yml_XXX.save(File(YML_FILE_PATH))
    }
//######################################################################################################################
//######################################################################################################################
//All_Command
    override fun onCommand(cmd_sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {

        when (cmd.name){
            "setyml" -> {
                //>>>   /setyml normal File_Path configuration value
                //>>>   /setyml list_index_n File_Path configuration value
                if (args.isEmpty()) return true
                if (args.lastIndex >= 3){
                    Yml_X = YamlConfiguration.loadConfiguration(File(args[1]))
                    Yml_R = YamlConfiguration.loadConfiguration(File("$main_dir/TEMP.yml"))

                    if (args[0].startsWith("normal")){

                        var STRING = ""
                        for (L_CONTENT_SIZE in 0..(args.size - 1 - 3)){
                            if (L_CONTENT_SIZE > 0) STRING = STRING + " "
                            STRING = STRING + args[3 + L_CONTENT_SIZE]
                        }
                        Yml_X[args[2]] = STRING
                        Yml_X.save(File(args[1]))
                    }
                    if (args[0].startsWith("list_add")){
                        var ARR_LIST = ArrayList<String>()
                        if (Yml_X[args[2]] != null) ARR_LIST = Yml_X[args[2]] as ArrayList<String>

                        var STRING = ""
                        for (L_CONTENT_SIZE in 0..(args.size - 1 - 3)){
                            if (L_CONTENT_SIZE > 0) STRING = STRING + " "
                            STRING = STRING + args[3 + L_CONTENT_SIZE]
                        }
                        ARR_LIST.add(STRING)
                        Yml_X[args[2]] = ARR_LIST
                        Yml_X.save(File(args[1]))
                    }
                }
            }
            "rui" -> {
                if (args.isEmpty()) return true
                //rui open_for_player Player_UUID GUI_FILE_NAME GUI_Title
                if (args.size >= 4){
                    if (args[0] == "open_for_player"){
                        var PLAYERS_ONLINE = Bukkit.getOnlinePlayers()
                        if (Bukkit.getPlayer(UUID.fromString(args[1])) != null ){
                            Yml_1 = YamlConfiguration.loadConfiguration(File("$main_dir/UI/" + args[2] + ".yml"))
                            //open VVV
                            menu_gui_1 = Bukkit.createInventory(null, Yml_1["Surface_UI.Size"] as Int, "" + args[3])

                            var content = (Yml_1["Surface_UI.Contents"] as List<ItemStack?>).toTypedArray()
                            menu_gui_1.setContents(content)
                            Bukkit.getPlayer(UUID.fromString(args[1]))!!.openInventory(menu_gui_1)
                        }
                    }
                }

                if (cmd_sender is Player){
                    Player_In_Event = cmd_sender

                    //rui create GUI_FILE_NAME size
                    if (args[0] == "create"){
                        if (args.lastIndex == 2){

                            if (isInt(args[2])){
                                if (!File("$main_dir/UI/" + args[1] + ".yml").exists()){

                                    menu_gui_1 = Bukkit.createInventory(null, args[2].toInt(), "                                                                                                                                                                            <===>GUI_EDIT<===>___<===>GUI_FILE_NAME>>>" + args[1] + ".yml<===>")
                                    Yml_1 = YamlConfiguration.loadConfiguration(File("$main_dir/UI/" + args[1] + ".yml"))

                                    Player_In_Event!!.openInventory(menu_gui_1)

                                    setYmlValue("$main_dir/UI/" + args[1] + ".yml","Surface_UI.Contents",Player_In_Event!!.openInventory.topInventory.contents)
                                    setYmlValue("$main_dir/UI/" + args[1] + ".yml","Surface_UI.Size",Player_In_Event!!.openInventory.topInventory.size)

                                }
                            }
                        }
                    }
                    //Open_And_Edit_GUI_From_File
                    //rui edit GUI_FILE_NAME (size)
                    if (args[0] == "edit"){
                        if (args.lastIndex >= 1){
                            if (File("$main_dir/UI/" + args[1] + ".yml").exists()){
                                Yml_1 = YamlConfiguration.loadConfiguration(File("$main_dir/UI/" + args[1] + ".yml"))

                                //open VVV
                                if (args.lastIndex == 1){
                                    menu_gui_1 = Bukkit.createInventory(null, Yml_1["Surface_UI.Size"] as Int, "                                                                                                                                                                            <===>GUI_EDIT<===>___<===>GUI_FILE_NAME>>>" + args[1] + ".yml<===>")

                                    var content = (Yml_1["Surface_UI.Contents"] as List<ItemStack?>).toTypedArray()
                                    menu_gui_1.setContents(content)
                                    Player_In_Event!!.openInventory(menu_gui_1)
                                }
                                //size VVV
                                if (args.lastIndex == 2){

                                    if (isInt(args[2])){

                                        menu_gui_1 = Bukkit.createInventory(null, args[2].toInt(), "                                                                                                                                                                            <===>GUI_EDIT<===>___<===>GUI_FILE_NAME>>>" + args[1] + ".yml<===>")

                                        var content = (Yml_1["Surface_UI.Contents"] as List<ItemStack?>).toTypedArray()
                                        menu_gui_1.setContents(content)
                                        Player_In_Event!!.openInventory(menu_gui_1)

                                    }
                                }
                            }
                        }
                    }
                }
            }
            "system_message" -> {
                //system_message add UUID Alive_Time STRING.....
                if (args.isEmpty()) return true
                if (args.lastIndex >= 4){
                    if (args[0] == "add"){
                        if (File("$main_dir/Player_DATA/" + args[1] + "/Channel_And_Message.yml").exists()){
                            var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/" + args[1] + "/Channel_And_Message.yml"))
                            var SYS_MSG = ""

                            for(i in 0 .. (args.lastIndex - 4)){
                                SYS_MSG = SYS_MSG + args[i + 4] + " "
                            }
                            if (args[3] == "dummy"){
                                SYS_MSG = SYS_MSG + "<===>Time_When_Sent===>>>" + System.currentTimeMillis() + "<===>___<===>Alive_Time===>>>NAN<===>___<===>Msg_Tag===>>>" + args[2]

                            }else{
                                SYS_MSG = SYS_MSG + "<===>Time_When_Sent===>>>" + System.currentTimeMillis() + "<===>___<===>Alive_Time===>>>" + args[3] + "<===>___<===>Msg_Tag===>>>" + args[2]
                            }
                            var SYS_MSG_LIST = ArrayList<String>()
                            if (Player_DATA_CH["SystemMessage.Strings"] != null) SYS_MSG_LIST = Player_DATA_CH["SystemMessage.Strings"] as ArrayList<String>
                            SYS_MSG_LIST.add(0,SYS_MSG)
                            setYmlValue("$main_dir/Player_DATA/" + args[1] + "/Channel_And_Message.yml","SystemMessage.Strings",SYS_MSG_LIST)

                        }
                    }
                }
                if (args.lastIndex >= 2){
                    if (args[0] == "remove"){
                        if (File("$main_dir/Player_DATA/" + args[1] + "/Channel_And_Message.yml").exists()){
                            var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/" + args[1] + "/Channel_And_Message.yml"))

                            var SYS_MSG_LIST = ArrayList<String>()
                            if (Player_DATA_CH["SystemMessage.Strings"] != null) SYS_MSG_LIST = Player_DATA_CH["SystemMessage.Strings"] as ArrayList<String>

                            for (i in SYS_MSG_LIST.indices){
                                if (SYS_MSG_LIST[i].split("<===>Msg_Tag===>>>")[1] == args[2] ){
                                    SYS_MSG_LIST.removeAt(i)
                                }
                            }
                            setYmlValue("$main_dir/Player_DATA/" + args[1] + "/Channel_And_Message.yml","SystemMessage.Strings",SYS_MSG_LIST)

                        }
                    }
                }
            }
            "chatbox_update" -> {

                if (args.isEmpty()) return true

                if (args.lastIndex >= 0){
                    CURRENT_RECIPIENT_UUID = args[0]

                    //Yml_X = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA.yml"))
                    if (File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Common.yml").exists()){
                        var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Channel_And_Message.yml"))
                        var Player_DATA_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Common.yml"))

                        CURRENT_RECIPIENT_NAME = Player_DATA_COM["UserName"] as String


                        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(CURRENT_RECIPIENT_NAME))){

                            var THE_SYS_MSG = Player_DATA_CH["SystemMessage.Strings"] as ArrayList<String>
                            if (!THE_SYS_MSG.isNullOrEmpty()) {
                                for (i in THE_SYS_MSG.lastIndex downTo 0){
                                    if (THE_SYS_MSG[i].contains("<===>Time_When_Sent===>>>") && THE_SYS_MSG[i].contains("<===>Alive_Time===>>>")){
                                        var Time_When_Sent = THE_SYS_MSG[i].split("<===>Time_When_Sent===>>>")[1].split("<===>")[0]
                                        var Alive_Time = THE_SYS_MSG[i].split("<===>Alive_Time===>>>")[1].split("<===>")[0]

                                        if (Alive_Time != "NAN"){
                                            if (System.currentTimeMillis() - Time_When_Sent.toLong() >= Alive_Time.toLong()){
                                                THE_SYS_MSG.removeAt(i)

                                                setYmlValue("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Channel_And_Message.yml","SystemMessage.Strings",THE_SYS_MSG)
                                                Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Channel_And_Message.yml"))

                                            }
                                        }
                                    }
                                }
                            }
                            //>>>channel_0
                            if (Player_DATA_CH["Channel.CurrentChannel"] == "0"){
                                var Yml_SSS = YamlConfiguration.loadConfiguration(File("$main_dir/Chat/Global_Chat_REC.yml"))
                                if (Yml_SSS["Contents"] == null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [\"" + "\\n0\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n" + "\"]")
                                if (Yml_SSS["Contents"] != null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [\"" + Yml_SSS["Contents"] + "\"]")

                                var THE_SYS_MSG = Player_DATA_CH["SystemMessage.Strings"] as ArrayList<String>
                                if (!THE_SYS_MSG.isNullOrEmpty()){

                                    for (i in THE_SYS_MSG.lastIndex downTo 0){
                                        var THE_SYS_MSG_STRING = THE_SYS_MSG[i].split("<===>Time_When_Sent===>>>")[0]
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [{\"text\":\"" + THE_SYS_MSG_STRING + "\",\"font\":\"uniform\"}]")

                                    }
                                }
                            }
                            //>>>channel_1
                            if (Player_DATA_CH["Channel.CurrentChannel"] == "1"){

                                var REG_CHAT = YamlConfiguration.loadConfiguration(File("$main_dir/Chat/RegionChat/$CURRENT_RECIPIENT_UUID.yml"))
                                if (REG_CHAT["RegionChat.Contents"] == null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [\"" + "\\n1\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n" + "\"]")
                                if (REG_CHAT["RegionChat.Contents"] != null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [\"" + REG_CHAT["RegionChat.Contents"] + "\"]")

                                var THE_SYS_MSG = Player_DATA_CH["SystemMessage.Strings"] as ArrayList<String>
                                if (!THE_SYS_MSG.isNullOrEmpty()){

                                    for (i in THE_SYS_MSG.lastIndex downTo 0){
                                        var THE_SYS_MSG_STRING = THE_SYS_MSG[i].split("<===>Time_When_Sent===>>>")[0]
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [{\"text\":\"" + THE_SYS_MSG_STRING + "\",\"font\":\"uniform\"}]")

                                    }
                                }
                            }
                            //>>>channel_2
                            if (Player_DATA_CH["Channel.CurrentChannel"] == "2"){
                                //Yml_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Chat/Private.yml"))


                                var CurrentContact_UUID = ""
                                if (Player_DATA_CH["PrivateChat.CurrentContact"] != null) CurrentContact_UUID = "" + Player_DATA_CH["PrivateChat.CurrentContact"]
                                var Contacts = ArrayList<String>()
                                var Contacts_Size = 0
                                if (Player_DATA_CH["PrivateChat.CurrentContact"] != null){
                                    Contacts = Player_DATA_CH["PrivateChat.Contacts"] as ArrayList<String>
                                    Contacts_Size = Contacts.size
                                }
                                var Contents = ""

                                var PrivateChat_File_Path = ""
                                if (File("$main_dir/Chat/PrivateChat/" + CURRENT_RECIPIENT_UUID + "___" + CurrentContact_UUID + ".yml").exists()) PrivateChat_File_Path = "$main_dir/Chat/PrivateChat/" + CURRENT_RECIPIENT_UUID + "___" + CurrentContact_UUID + ".yml"
                                if (File("$main_dir/Chat/PrivateChat/" + CurrentContact_UUID + "___" + CURRENT_RECIPIENT_UUID + ".yml").exists()) PrivateChat_File_Path = "$main_dir/Chat/PrivateChat/" + CurrentContact_UUID + "___" + CURRENT_RECIPIENT_UUID + ".yml"

                                var Yml_PFP = YamlConfiguration.loadConfiguration(File(PrivateChat_File_Path))
                                if (PrivateChat_File_Path != "" ) Contents = "" + Yml_PFP["PrivateChat.Contents"] + ""

                                var THE_OUTPUT = ""
                                var THE_INPUT_LIST = listOf<String>()
                                var THE_INPUT_LIST_SIZE = 0
                                if (Contents.contains("\\n")) THE_INPUT_LIST = Contents.split("\\n")
                                if (Contents.contains("\\n")) THE_INPUT_LIST_SIZE = Contents.split("\\n").size

                                //if (Player_DATA_1["" + CURRENT_RECIPIENT_UUID + ".SystemMessage.String"] != null) ADDON_LINE = 1

                                var THE_SYS_MSG = Player_DATA_CH["SystemMessage.Strings"] as ArrayList<String>
                                var ADDON_INDEX = 0
                                if (!THE_SYS_MSG.isNullOrEmpty() )ADDON_INDEX = THE_SYS_MSG.lastIndex

                                for (i in 99 downTo 0){

                                    if (Contacts.isNullOrEmpty()){
                                        THE_OUTPUT = THE_OUTPUT + "\",{\"text\":\"\\n\\u00a7l⎝\\u00a7r \\u00a7r \\u00a7r \\u00a7l \\u00a76\\u00a7lￚ\\u00a7r\\u00a76ￚￚￚ\\u00a76\\u00a7lￚ\\u00a7r \\u00a7r \\u00a7r \\u00a7l \\u00a7l⎠\",\"color\":\"#834701\",\"font\":\"uniform\"},\""

                                        if (!THE_SYS_MSG.isNullOrEmpty()){
                                            if (i <= ADDON_INDEX ){
                                                var THE_SYS_MSG_STRING = THE_SYS_MSG[i].split("<===>Time_When_Sent===>>>")[0]
                                                THE_OUTPUT = THE_OUTPUT + "\",{\"text\":\"" + " " + THE_SYS_MSG_STRING + "\",\"font\":\"uniform\"},\""

                                            }
                                        }
                                    }

                                    if (!Contacts.isNullOrEmpty()){

                                        if ((Contacts_Size - 1 - i) < 0) THE_OUTPUT = THE_OUTPUT + "\",{\"text\":\"\\n\\u00a7l⎝\\u00a7r \\u00a7r \\u00a7r \\u00a7l \\u00a76\\u00a7lￚ\\u00a7r\\u00a76ￚￚￚ\\u00a76\\u00a7lￚ\\u00a7r \\u00a7r \\u00a7r \\u00a7l \\u00a7l⎠\",\"color\":\"#834701\",\"font\":\"uniform\"},\""
                                        if ((Contacts_Size - 1 - i) >= 0){

                                            var UNREAD_TAG_COLOR = "#7A7A7A"
                                            var NAME_COLOR = "gold"
                                            var Player_DATA_COM_CONTACT = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/" + Contacts[i] + "/Common.yml"))
                                            var Contacts_NAME_INPUT = "" + Player_DATA_COM_CONTACT["UserName"]
                                            if (Contacts[i] == CurrentContact_UUID) NAME_COLOR = "dark_green"
                                            if (Contacts[i] != CurrentContact_UUID){
                                                var THE_TAGS = Player_DATA_COM["Tags"] as ArrayList<String>
                                                if (THE_TAGS.contains("UNREAD_CONTACT_" + Contacts[i])) UNREAD_TAG_COLOR = "#E42F2F"
                                            }

                                            var Contacts_NAME_OUTPUT = Contacts_NAME_INPUT.replace("a","A").replace("b","B").replace("c","C").replace("d","D").replace("e","E").replace("f","F").replace("g","G").replace("h","H").replace("i","Ꮖ").replace("I","Ꮖ").replace("j","J").replace("k","K").replace("l","L").replace("m","M").replace("n","N").replace("o","O").replace("p","P").replace("q","Q").replace("r","R").replace("s","S").replace("t","T").replace("u","U").replace("v","V").replace("w","W").replace("x","X").replace("y","Y").replace("z","Z").replace("1","ไ")

                                            var CON_LEN = Contacts_NAME_OUTPUT.length
                                            if (Contacts_NAME_OUTPUT.length > 8) Contacts_NAME_OUTPUT = Contacts_NAME_OUTPUT.substring(0,8) + "⋯ "
                                            if (Contacts_NAME_OUTPUT.length <= 8){
                                                if (CON_LEN < 8){
                                                    for (L in 1..(8 - CON_LEN)){
                                                        Contacts_NAME_OUTPUT = Contacts_NAME_OUTPUT + "\\u00a7r "
                                                    }
                                                }
                                                Contacts_NAME_OUTPUT = Contacts_NAME_OUTPUT + "\\u00a7l \\u00a7r "
                                            }

                                            THE_OUTPUT = THE_OUTPUT + "\\n\",\"" + "\",{\"text\":\"⎝\",\"bold\":true,\"color\":\"#834701\"},{\"text\":\"❖\",\"color\":\"" + UNREAD_TAG_COLOR + "\"},{\"font\":\"uniform\",\"text\":\"" + Contacts_NAME_OUTPUT + "\",\"color\":\"" + NAME_COLOR + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/privatechatselect " + Contacts[i] + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Chat with " + Contacts_NAME_INPUT + "\",\"color\":\"dark_green\"}]}},{\"text\":\"❐\",\"color\":\"#00B38F\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/interactiongui " + Contacts[i] + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Click here to Open Interaction Gui\",\"color\":\"dark_green\"}]}},{\"text\":\"⎠\",\"bold\":true,\"color\":\"#834701\"},\""

                                            if (THE_INPUT_LIST_SIZE == 0){
                                                if (!THE_SYS_MSG.isNullOrEmpty()){
                                                    if (i <= ADDON_INDEX ){
                                                        var THE_SYS_MSG_STRING = THE_SYS_MSG[i].split("<===>Time_When_Sent===>>>")[0]
                                                        THE_OUTPUT = THE_OUTPUT + "\",{\"text\":\"" + " " + THE_SYS_MSG_STRING + "\",\"font\":\"uniform\"},\""

                                                    }
                                                }
                                            }
                                        }
                                        if ((THE_INPUT_LIST_SIZE > i)){
                                            if (THE_SYS_MSG.isNullOrEmpty()) THE_OUTPUT = THE_OUTPUT + THE_INPUT_LIST[THE_INPUT_LIST.lastIndex - i]
                                            if (!THE_SYS_MSG.isNullOrEmpty()){
                                                if (i > ADDON_INDEX){
                                                    THE_OUTPUT = THE_OUTPUT + THE_INPUT_LIST[THE_INPUT_LIST.lastIndex - i + THE_SYS_MSG.size]
                                                }
                                                if (i <= ADDON_INDEX){
                                                    var THE_SYS_MSG_STRING = THE_SYS_MSG[i].split("<===>Time_When_Sent===>>>")[0]
                                                    THE_OUTPUT = THE_OUTPUT + "\",{\"text\":\"" + " " + THE_SYS_MSG_STRING + "\",\"font\":\"uniform\"},\""
                                                }
                                            }
                                        }
                                    }
                                }
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [\"" + THE_OUTPUT + "\"]")
                            }
                            //======================================================================================
                            //======================================================================================
                            //footer_update
                            Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Channel_And_Message.yml"))
                            var CURRENT_CHANNEL = Player_DATA_CH["Channel.CurrentChannel"]

                            Player_DATA_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Common.yml"))
                            var RECIPIENT_TAGS = ArrayList<String>()
                            RECIPIENT_TAGS = Player_DATA_COM["Tags"] as ArrayList<String>

                            var LABEL_DISPLAYNAME = ""
                            var TEXT_COLOR = "gold"
                            var THE_HOVER = ""
                            var CHANNEL_LABEL = ""
                            var ALARM_ICON = "❖"
                            var ALARM_ICON_COLOR_CODE = "#7A7A7A"
                            var IS_BOLD = "false"

                            for (i in 0..7){

                                TEXT_COLOR = "gold"
                                ALARM_ICON_COLOR_CODE = "#7A7A7A"
                                if (RECIPIENT_TAGS.contains("UNREAD_CHANNEL_$i")) ALARM_ICON_COLOR_CODE = "#E42F2F"

                                if (i == 0){
                                    LABEL_DISPLAYNAME = "ᎪⅬⅬ"
                                    THE_HOVER = "Global Message"
                                    ALARM_ICON = "❖"
                                }
                                if (i == 1){
                                    LABEL_DISPLAYNAME = "ᎡᎬᏀⅠⲞⲚ"
                                    THE_HOVER = "Chat with player within 100 block radius"
                                    ALARM_ICON = "❖"
                                }
                                if (i == 2){
                                    LABEL_DISPLAYNAME = "ᏢᎡᏆᏙᎪᎢᎬ"
                                    THE_HOVER = "Chat with Someone"
                                    ALARM_ICON = "❖"
                                }
                                if (i == 3){
                                    LABEL_DISPLAYNAME = "ᏢᎪᎡᎢᎩ"
                                    THE_HOVER = "Chat with Party member"
                                    ALARM_ICON = "❖"
                                }
                                if (i == 4){
                                    LABEL_DISPLAYNAME = "ᏀꀎᏆⅬᎠ"
                                    THE_HOVER = "Chat with Guild member"
                                    ALARM_ICON = "❖"
                                }
                                if (i == 5){
                                    LABEL_DISPLAYNAME = "ⲚⲞᎢⅠᏟᎬ"
                                    THE_HOVER = "Notification such as invitation of friend , party , guild ... and system message....."
                                    ALARM_ICON = "❖"
                                }
                                if (i == 6){
                                    LABEL_DISPLAYNAME = "✗"
                                    THE_HOVER = "No-Message Channel"
                                    TEXT_COLOR = "#8E0101"
                                    ALARM_ICON = ""
                                }
                                if (i == 7){
                                    LABEL_DISPLAYNAME = "⚙"
                                    THE_HOVER = "Setting"
                                    TEXT_COLOR = "#00B38F"
                                    ALARM_ICON = ""
                                    IS_BOLD ="true"
                                }
                                if (i.toString() == CURRENT_CHANNEL) TEXT_COLOR = "dark_green"

                                CHANNEL_LABEL = CHANNEL_LABEL + ",{\"text\":\"⎛\",\"bold\":true,\"color\":\"#834701\"},{\"text\":\"" + ALARM_ICON + "\",\"color\":\"" + ALARM_ICON_COLOR_CODE + "\"},{\"text\":\"" + LABEL_DISPLAYNAME + "\",\"bold\":" + IS_BOLD + ",\"color\":\"" + TEXT_COLOR + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/channel " + i + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"" + THE_HOVER + "\",\"color\":\"dark_green\"}]}},{\"text\":\"⎞\",\"bold\":true,\"color\":\"#834701\"}"

                            }
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [{\"text\":\"▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭\\u00a7l▬▭▬▭\\u00a7r▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬\",\"color\":\"#834701\",\"font\":\"uniform\"}]")
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [\"\"" + CHANNEL_LABEL + "]")

                            //======================================================================================
                            //======================================================================================

                        }
                    }
                }
            }
            "footer_update" ->{


                if (args.isEmpty()) return true

                if (args.lastIndex >= 0){
                    CURRENT_RECIPIENT_UUID = args[0]
                    var Player_DATA_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Common.yml"))

                    //Yml_X = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA.yml"))
                    if (File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Common.yml").exists()){

                        CURRENT_RECIPIENT_NAME = Player_DATA_COM["UserName"] as String


                        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(CURRENT_RECIPIENT_NAME))){


                            //======================================================================================
                            //======================================================================================
                            //footer_update
                            var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Channel_And_Message.yml"))
                            var CURRENT_CHANNEL = Player_DATA_CH["Channel.CurrentChannel"]

                            var RECIPIENT_TAGS = ArrayList<String>()
                            RECIPIENT_TAGS = Player_DATA_COM["Tags"] as ArrayList<String>

                            var LABEL_DISPLAYNAME = ""
                            var TEXT_COLOR = "gold"
                            var THE_HOVER = ""
                            var CHANNEL_LABEL = ""
                            var ALARM_ICON = "❖"
                            var ALARM_ICON_COLOR_CODE = "#7A7A7A"
                            var IS_BOLD = "false"

                            for (i in 0..7){

                                TEXT_COLOR = "gold"
                                ALARM_ICON_COLOR_CODE = "#7A7A7A"
                                if (RECIPIENT_TAGS.contains("UNREAD_CHANNEL_$i")) ALARM_ICON_COLOR_CODE = "#E42F2F"

                                if (i == 0){
                                    LABEL_DISPLAYNAME = "ᎪⅬⅬ" ; THE_HOVER = "Global Message" ; ALARM_ICON = "❖"
                                }
                                if (i == 1){
                                    LABEL_DISPLAYNAME = "ᎡᎬᏀⅠⲞⲚ" ; THE_HOVER = "Chat with player within 100 block radius" ; ALARM_ICON = "❖"
                                }
                                if (i == 2){
                                    LABEL_DISPLAYNAME = "ᏢᎡᏆᏙᎪᎢᎬ" ; THE_HOVER = "Chat with Someone" ; ALARM_ICON = "❖"
                                }
                                if (i == 3){
                                    LABEL_DISPLAYNAME = "ᏢᎪᎡᎢᎩ" ; THE_HOVER = "Chat with Party member" ; ALARM_ICON = "❖"
                                }
                                if (i == 4){
                                    LABEL_DISPLAYNAME = "ᏀꀎᏆⅬᎠ" ; THE_HOVER = "Chat with Guild member" ; ALARM_ICON = "❖"
                                }
                                if (i == 5){
                                    LABEL_DISPLAYNAME = "ⲚⲞᎢⅠᏟᎬ" ; THE_HOVER = "Notification such as invitation of friend , party , guild ... and system message....." ; ALARM_ICON = "❖"
                                }
                                if (i == 6){
                                    LABEL_DISPLAYNAME = "✗" ; THE_HOVER = "No-Message Channel" ; TEXT_COLOR = "#8E0101" ; ALARM_ICON = ""
                                }
                                if (i == 7){
                                    LABEL_DISPLAYNAME = "⚙" ; THE_HOVER = "Setting" ; TEXT_COLOR = "#00B38F" ; ALARM_ICON = "" ; IS_BOLD ="true"
                                }
                                if (i.toString() == CURRENT_CHANNEL) TEXT_COLOR = "dark_green"

                                CHANNEL_LABEL = CHANNEL_LABEL + ",{\"text\":\"⎛\",\"bold\":true,\"color\":\"#834701\"},{\"text\":\"" + ALARM_ICON + "\",\"color\":\"" + ALARM_ICON_COLOR_CODE + "\"},{\"text\":\"" + LABEL_DISPLAYNAME + "\",\"bold\":" + IS_BOLD + ",\"color\":\"" + TEXT_COLOR + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/channel " + i + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"" + THE_HOVER + "\",\"color\":\"dark_green\"}]}},{\"text\":\"⎞\",\"bold\":true,\"color\":\"#834701\"}"

                            }
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [{\"text\":\"▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭\\u00a7l▬▭▬▭\\u00a7r▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬▭▬\",\"color\":\"#834701\",\"font\":\"uniform\"}]")
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + CURRENT_RECIPIENT_NAME + " [\"\"" + CHANNEL_LABEL + "]")

                            //======================================================================================
                            //======================================================================================

                        }
                    }
                }

            }
        }

        if (cmd_sender is Player) {
            when (cmd.name) {
                "pfi" -> {
                    if (args.isEmpty()) return true
                    if (args.size == 1){
                        Thread{
                            val AXX = Bukkit.getOfflinePlayer(args[0]).uniqueId

                            cmd_sender.sendMessage("" + AXX)
                        }.start()
                    }
                }

                "channel" -> {
                    if (args.isEmpty()) return true
                    Player_In_Event = cmd_sender
                    Player_UUID = Player_In_Event!!.uniqueId.toString()
                    CURRENT_RECIPIENT_NAME = Player_In_Event!!.name
                    CURRENT_RECIPIENT_UUID = Player_In_Event!!.uniqueId.toString()
                    var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Channel_And_Message.yml"))
                    var pre_channel =  Player_DATA_CH["Channel.CurrentChannel"] as String
                    MC_Version_Detection()


                    if (isInt(args[0])){

                        var CH_NUM = args[0].toInt()
                        REMOVE_PLAYER_TAGS(Player_UUID,"UNREAD_CHANNEL_" + args[0])

                        if (args[0] != "5" && args[0] != "7"){
                            setYmlValue("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Channel_And_Message.yml","Channel.CurrentChannel",args[0])
                        }
                        if (args[0] == "5"){
                            Player_DATA_CH["Channel.IsInNoticeGUI"] = "True"

                            //GUI_Page = 1
                            if (Player_In_Event!!.openInventory.title.contains("<===>Notice_GUI<===>")){

                            }else{
                                GUI_Page = 1
                            }

                            //GUI_PAGE
                            var MY_NOTICE_LIST = ArrayList<String>()
                            if (!(Player_DATA_CH["Channel.Notice"] as ArrayList<String>).isNullOrEmpty()){
                                MY_NOTICE_LIST = Player_DATA_CH["Channel.Notice"] as ArrayList<String>
                                if ((MY_NOTICE_LIST.lastIndex/42) + 1 < GUI_Page) GUI_Page = (MY_NOTICE_LIST.lastIndex/42) + 1
                                if (GUI_Page < 1) GUI_Page = 1
                            }

                            //Open_GUI_From_File
                            menu_gui_1 = Bukkit.createInventory(null,54,"§2§lNotice                                                                                                                                                                                                                                                     <===>GUI_PAGE>>>$GUI_Page<===>___<===>Notice_GUI<===>___<===>Locked_slot>>>0_1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24_25_26_27_28_29_30_31_32_33_34_35_36_37_38_39_40_41_42_43_44_45_46_47_48_49_50_51_52_53_54_55_56_57_58_59_60_61_62_63_64_65_66_67_68_69_70_71_72_73_74_75_76_77_78_79_80_81_82_83_84_85_86_87_88_89<===>___<===>Shift_locked_from_player_inv<===>")
                            Yml_2 = YamlConfiguration.loadConfiguration(File("$main_dir/UI/List_Type_Gui.yml"))
                            var content = (Yml_2["Surface_UI.Contents"] as List<ItemStack?>).toTypedArray()
                            menu_gui_1.setContents(content)


                            if (!(Player_DATA_CH["Channel.Notice"] as ArrayList<String>).isNullOrEmpty()){
                                MY_NOTICE_LIST = Player_DATA_CH["Channel.Notice"] as ArrayList<String>


                                XXX_LOOP@for(i in 42*(GUI_Page - 1)..(42*(GUI_Page - 1) + 41)){
                                    // 0 ~ 41
                                    var slot = i - (42 * (i/42))

                                    //friend request VVV
                                    if (MY_NOTICE_LIST[i].contains("_whishes_to_be_your_friend")){

                                        //
                                        if (File("$main_dir/Player_DATA/" + MY_NOTICE_LIST[i].split("_whishes_to_be_your_friend")[0] + "/Channel_And_Message.yml").exists()){
                                            var SENDER_UUID = MY_NOTICE_LIST[i].split("_whishes_to_be_your_friend")[0]
                                            var Player_DATA_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$SENDER_UUID/Common.yml"))
                                            var SENDER_NAME = Player_DATA_COM["UserName"]

                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "paper{display:{Name:'{\"text\":\"" + SENDER_NAME + " wishes to be your friend" + "\",\"font\":\"uniform\",\"color\":\"#4EC73C\",\"bold\":true,\"italic\":false}',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false}','{\"text\":\"[Left-Click] to Accept\",\"font\":\"uniform\",\"color\":\"#4EC73C\",\"bold\":true,\"italic\":false}','{\"text\":\"[Right-Click] to Deny\",\"font\":\"uniform\",\"color\":\"red\",\"bold\":true,\"italic\":false}']}} 1")
                                            ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
                                            ITEMSTACK_mdf = ITEMSTACK.itemMeta
                                            ITEMSTACK_mdf!!.setLocalizedName(MY_NOTICE_LIST[i])

                                            if (MY_NOTICE_LIST[i].split("_whishes_to_be_your_friend")[1] == "_UNREAD"){
                                                ITEMSTACK_mdf!!.addEnchant(Enchantment.KNOCKBACK,1,true)
                                                ITEMSTACK_mdf!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                                            }
                                            ITEMSTACK.itemMeta = ITEMSTACK_mdf

                                            menu_gui_1.setItem((slot + 2* (slot/7)),ITEMSTACK)
                                        }
                                    }

                                    //Party invatation VVV
                                    if (MY_NOTICE_LIST[i].contains("_whishes_to_invite_you_to_join_the_party")){

                                        //
                                        if (File("$main_dir/Player_DATA/" + MY_NOTICE_LIST[i].split("_whishes_to_invite_you_to_join_the_party")[0] + "/Channel_And_Message.yml").exists()){
                                            var SENDER_UUID = MY_NOTICE_LIST[i].split("_whishes_to_invite_you_to_join_the_party")[0]
                                            var Player_DATA_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$SENDER_UUID/Common.yml"))
                                            var SENDER_NAME = Player_DATA_COM["UserName"]

                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "paper{display:{Name:'{\"text\":\"" + SENDER_NAME + " wishes to invite you to join the party" + "\",\"font\":\"uniform\",\"color\":\"#4EC73C\",\"bold\":true,\"italic\":false}',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false}','{\"text\":\"[Left-Click] to Accept\",\"font\":\"uniform\",\"color\":\"#4EC73C\",\"bold\":true,\"italic\":false}','{\"text\":\"[Right-Click] to Deny\",\"font\":\"uniform\",\"color\":\"red\",\"bold\":true,\"italic\":false}']}} 1")
                                            ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
                                            ITEMSTACK_mdf = ITEMSTACK.itemMeta
                                            ITEMSTACK_mdf!!.setLocalizedName(MY_NOTICE_LIST[i])

                                            if (MY_NOTICE_LIST[i].split("_whishes_to_invite_you_to_join_the_party")[1] == "_UNREAD"){
                                                ITEMSTACK_mdf!!.addEnchant(Enchantment.KNOCKBACK,1,true)
                                                ITEMSTACK_mdf!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                                            }
                                            ITEMSTACK.itemMeta = ITEMSTACK_mdf

                                            menu_gui_1.setItem((slot + 2* (slot/7)),ITEMSTACK)
                                        }
                                    }

                                    //Party Joining request VVV
                                    if (MY_NOTICE_LIST[i].contains("_whishes_to_join_your_party")){

                                        //
                                        if (File("$main_dir/Player_DATA/" + MY_NOTICE_LIST[i].split("_whishes_to_join_your_party")[0] + "/Channel_And_Message.yml").exists()){
                                            var SENDER_UUID = MY_NOTICE_LIST[i].split("_whishes_to_join_your_party")[0]
                                            var Player_DATA_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$SENDER_UUID/Common.yml"))
                                            var SENDER_NAME = Player_DATA_COM["UserName"]

                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "paper{display:{Name:'{\"text\":\"" + SENDER_NAME + " wishes to join your party" + "\",\"font\":\"uniform\",\"color\":\"#4EC73C\",\"bold\":true,\"italic\":false}',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false}','{\"text\":\"[Left-Click] to Accept\",\"font\":\"uniform\",\"color\":\"#4EC73C\",\"bold\":true,\"italic\":false}','{\"text\":\"[Right-Click] to Deny\",\"font\":\"uniform\",\"color\":\"red\",\"bold\":true,\"italic\":false}']}} 1")
                                            ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
                                            ITEMSTACK_mdf = ITEMSTACK.itemMeta
                                            ITEMSTACK_mdf!!.setLocalizedName(MY_NOTICE_LIST[i])

                                            if (MY_NOTICE_LIST[i].split("_whishes_to_join_your_party")[1] == "_UNREAD"){
                                                ITEMSTACK_mdf!!.addEnchant(Enchantment.KNOCKBACK,1,true)
                                                ITEMSTACK_mdf!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                                            }
                                            ITEMSTACK.itemMeta = ITEMSTACK_mdf

                                            menu_gui_1.setItem((slot + 2* (slot/7)),ITEMSTACK)
                                        }
                                    }
                                    if (i >= MY_NOTICE_LIST.lastIndex) break@XXX_LOOP
                                }
                            }
                            Player_In_Event!!.openInventory(menu_gui_1)
                        }
                        if (args[0] == "7"){
                            menu_gui_1 = Bukkit.createInventory(null,54,"§2§lChannel Setting                                                                                                                                                                                                                                                     <===>Channel_Setting<===>___<===>Locked_slot>>>0_1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24_25_26_27_28_29_30_31_32_33_34_35_36_37_38_39_40_41_42_43_44_45_46_47_48_49_50_51_52_53_54_55_56_57_58_59_60_61_62_63_64_65_66_67_68_69_70_71_72_73_74_75_76_77_78_79_80_81_82_83_84_85_86_87_88_89<===>___<===>Shift_locked_from_player_inv<===>")
                            var THE_CHANNEL_NAME = ""

                            for (i in 0..5){
                                if (i == 0){
                                    THE_CHANNEL_NAME = "All"
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + THE_CHANNEL_NAME + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_0_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_0_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;2018521520,1627735941,-1882266884,758719831],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0N2EzOTQ5OWRlNDllMjRjODkyYjA5MjU2OTQzMjkyN2RlY2JiNzM5OWUxMTg0N2YzMTA0ZmRiMTY1YjZkYyJ9fX0=\"}]}}} 1")
                                    menu_gui_1.setItem(10,Player_In_Event!!.enderChest.getItem(0))
                                }
                                if (i == 1){
                                    THE_CHANNEL_NAME = "Region"
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Region\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_1_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_1_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;1590212200,-317177619,-1968522945,379510964],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhmZDcxMjZjZDY3MGM3OTcxYTI4NTczNGVkZmRkODAyNTcyYTcyYTNmMDVlYTQxY2NkYTQ5NDNiYTM3MzQ3MSJ9fX0=\"}]}}} 1")
                                    menu_gui_1.setItem(13,Player_In_Event!!.enderChest.getItem(0))
                                }
                                if (i == 2){
                                    THE_CHANNEL_NAME = "Private"
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Private\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_2_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_2_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;-1199100648,-1480964910,-1232196606,889593674],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ0MGIwYWNiZjk4YzY3YzU1ZjIwYmMxOWQ5NDkxZGU0OWYzYzhhMjBkZTkwNWUzZmFkNWIzZGU4YjRkYjdiOCJ9fX0=\"}]}}} 1")
                                    menu_gui_1.setItem(16,Player_In_Event!!.enderChest.getItem(0))
                                }
                                if (i == 3){
                                    THE_CHANNEL_NAME = "Party"
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Party\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_3_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_3_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;-1672030017,-1658041606,-1468098958,-1280689632],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE1MzYxYjUyZGFmNGYxYzVjNTQ4MGEzOWZhYWExMDg5NzU5NWZhNTc2M2Y3NTdiZGRhMzk1NjU4OGZlYzY3OCJ9fX0=\"}]}}} 1")
                                    menu_gui_1.setItem(37,Player_In_Event!!.enderChest.getItem(0))
                                }
                                if (i == 4){
                                    THE_CHANNEL_NAME = "Guild"
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Guild\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_4_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_4_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;-1867535749,442057165,-1657564529,-401811555],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzYxODQ2MTBjNTBjMmVmYjcyODViYzJkMjBmMzk0MzY0ZTgzNjdiYjMxNDg0MWMyMzhhNmE1MjFhMWVlMTJiZiJ9fX0=\"}]}}} 1")
                                    menu_gui_1.setItem(40,Player_In_Event!!.enderChest.getItem(0))
                                }
                                if (i == 5){
                                    THE_CHANNEL_NAME = "Notice"
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Notice\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_5_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_5_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;-1059101233,27282105,-1508354492,1680594266],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFmNWRhZGQ1MGQzODk2OWQ2NWIxMDUyZjBiOTkzNTg2ZDAyNjk2NWQ5ZDVjYzJhMmYzYzc1NTViYjJlZTY0YSJ9fX0=\"}]}}} 1")
                                    menu_gui_1.setItem(43,Player_In_Event!!.enderChest.getItem(0))
                                }
                            }
                            Player_In_Event!!.openInventory(menu_gui_1)
                        }
                        if (args.size >= 2){
                            if (args[1] == "force_a78315"){
                                CHAT_BOX_UPDATE(Player_UUID)
                            }
                        }else if (pre_channel != args[0]){
                            CHAT_BOX_UPDATE(Player_UUID)
                        }

                    }
                }
                "privatechatselect" -> {
                    if (args.isEmpty()) return true
                    Player_In_Event = cmd_sender
                    Player_UUID = Player_In_Event!!.uniqueId.toString()

                    var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml"))
                    var PRE_CON = Player_DATA_CH["PrivateChat.CurrentContact"]
                    MC_Version_Detection()

                    if (args.isNotEmpty()){
                        if (File("$main_dir/Player_DATA/" + args[0] + "/Common.yml").exists()){
                            var Contacts = ArrayList<String>()
                            if (Player_DATA_CH["PrivateChat.Contacts"] != null) Contacts = Player_DATA_CH["PrivateChat.Contacts"] as ArrayList<String>

                            if (!Contacts.contains(args[0])){
                                Contacts.add(0,args[0])
                                setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","PrivateChat.Contacts",Contacts)
                                setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","PrivateChat.CurrentContact",args[0])

                            }
                            if (Contacts.contains(args[0])){

                                setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","PrivateChat.CurrentContact",args[0])
                                REMOVE_PLAYER_TAGS(Player_UUID,"UNREAD_CONTACT_" + args[0])

                                if (PRE_CON != args[0]) CHAT_BOX_UPDATE(Player_UUID)
                            }
                        }
                    }
                }
                "interactiongui" -> {
                    if (args.isEmpty()) return true
                    Player_In_Event = cmd_sender
                    Player_UUID = Player_In_Event!!.uniqueId.toString()
                    MC_Version_Detection()

                    if (args.isNotEmpty()){
                        var Player_DATA_FRI_L = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Friends_List.yml"))

                        if (args[0] != Player_In_Event!!.uniqueId.toString()){

                            if (File("$main_dir/Player_DATA/" + args[0] + "/Common.yml").exists()){
                                var Player_DATA_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/" + args[0] + "/Common.yml"))
                                Yml_2 = YamlConfiguration.loadConfiguration(File("$main_dir/UI/Interaction_Gui.yml"))

                                menu_gui_1 = Bukkit.createInventory(null, Yml_2["Surface_UI.Size"] as Int, "§5➤§2" + Player_DATA_COM["UserName"] + "                                                                                                                                                                                                                                                                                                                       <===>Interaction_Gui<===>___<===>Player_Name>>>" + Player_DATA_COM[".UserName"] + "<===>___<===>Player_UUID>>>" + args[0] + "<===>___<===>Locked_slot>>>0_1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24_25_26_27_28_29_30_31_32_33_34_35_36_37_38_39_40_41_42_43_44_45_46_47_48_49_50_51_52_53_54_55_56_57_58_59_60_61_62_63_64_65_66_67_68_69_70_71_72_73_74_75_76_77_78_79_80_81_82_83_84_85_86_87_88_89<===>___<===>Shift_locked_from_player_inv<===>")

                                var content = (Yml_2["Surface_UI.Contents"] as List<ItemStack?>).toTypedArray()
                                menu_gui_1.setContents(content)

                                //friend Icon
                                var FRIEND_LIST = Player_DATA_FRI_L["Friends"] as ArrayList<String>
                                if (FRIEND_LIST.contains(args[0])){

                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"[\",\"font\":\"uniform\",\"color\":\"red\",\"bold\":true,\"italic\":false},{\"text\":\"X\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"] remove friend\",\"font\":\"uniform\",\"color\":\"red\",\"bold\":true,\"italic\":false}]'},SkullOwner:{Id:[I;1590668272,1381189552,-1538418164,-1841266247],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==\"}]}}} 1")
                                    menu_gui_1.setItem(10,Player_In_Event!!.enderChest.getItem(0))
                                }

                                Player_In_Event!!.openInventory(menu_gui_1)
                            }
                        }
                    }
                }
                "friend" -> {
                    Player_In_Event = cmd_sender
                    Player_UUID = Player_In_Event!!.uniqueId.toString()
                    var Player_DATA_FRI_L = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Friends_List.yml"))
                    var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml"))

                    MC_Version_Detection()

                    if (args.isEmpty()) {

                        if (Player_In_Event!!.scoreboardTags.size > 0){
                            var had_ever_Opened = false
                            for (i in 0..Player_In_Event!!.scoreboardTags.size - 1){
                                if (Player_In_Event!!.scoreboardTags.toList()[i].contains("Friend_Page_at_Last_Time===>>>")){
                                    had_ever_Opened = true
                                    Bukkit.dispatchCommand(Player_In_Event!!,"friend list " + Player_In_Event!!.scoreboardTags.toList()[i].split("Friend_Page_at_Last_Time===>>>")[1])
                                    return true
                                }
                            }
                            if (had_ever_Opened == false){
                                Bukkit.dispatchCommand(Player_In_Event!!,"friend list 1")
                                return true
                            }
                        }
                        //return true
                    }
                    if (!args.isEmpty() && (args[0] == "add" || args[0] == "remove" || args[0] == "list") ){
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this, {
                            //Add_Friend
                            if (args[0] == "add"){
                                if (args.size >= 2){
                                    var THE_B_SIDE_UUID = ""
                                    var FRIEND_LIST = ArrayList<String>()
                                    FRIEND_LIST = Player_DATA_FRI_L["Friends"] as ArrayList<String>
                                    //==========================================================================================
                                    //VVV if arg_1 is UUID
                                    if (File("$main_dir/Player_DATA/" + args[1] + "/Common.yml").exists() ){
                                        THE_B_SIDE_UUID = args[1]
                                    }
                                    else{
                                        //VVV if arg_1 is username
                                        THE_B_SIDE_UUID = Bukkit.getOfflinePlayer("" + args[1]).uniqueId.toString()
                                    }
                                    //==========================================================================================
                                    if (File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Common.yml").exists() ){

                                        if (!FRIEND_LIST.contains(THE_B_SIDE_UUID)){

                                            //accept_friend_request
                                            var Player_DATA_FRI_L_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Friends_List.yml"))
                                            var Player_DATA_COM_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Common.yml"))
                                            var Player_DATA_CH_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Channel_And_Message.yml"))


                                            var A_SIDE_NOTICE_LIST = ArrayList<String>()
                                            if (!(Player_DATA_CH["Channel.Notice"] as ArrayList<String>).isNullOrEmpty()) A_SIDE_NOTICE_LIST = Player_DATA_CH["Channel.Notice"] as ArrayList<String>
                                            if (A_SIDE_NOTICE_LIST.contains("" + THE_B_SIDE_UUID + "_whishes_to_be_your_friend") || A_SIDE_NOTICE_LIST.contains("" + THE_B_SIDE_UUID + "_whishes_to_be_your_friend_UNREAD")){
                                                //==================================================================================================
                                                //Add A into B's Friend List
                                                var FRIEND_LIST = ArrayList<String>()
                                                FRIEND_LIST = Player_DATA_FRI_L_2["Friends"] as ArrayList<String>
                                                FRIEND_LIST.add(Player_UUID.toString())
                                                setYmlValue("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Friends_List.yml","Friends",FRIEND_LIST)
                                                //==================================================================================================
                                                //Add B into A's Friend List
                                                FRIEND_LIST = ArrayList<String>()
                                                FRIEND_LIST = Player_DATA_FRI_L["Friends"] as ArrayList<String>
                                                FRIEND_LIST.add(THE_B_SIDE_UUID)
                                                setYmlValue("$main_dir/Player_DATA/$Player_UUID/Friends_List.yml","Friends",FRIEND_LIST)
                                                //==================================================================================================

                                                A_SIDE_NOTICE_LIST.remove(THE_B_SIDE_UUID + "_whishes_to_be_your_friend")
                                                A_SIDE_NOTICE_LIST.remove(THE_B_SIDE_UUID + "_whishes_to_be_your_friend_UNREAD")
                                                setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Notice",A_SIDE_NOTICE_LIST)


                                                if (Player_In_Event!!.openInventory.title.contains("<===>Interaction_Gui<===>")){
                                                    if (Player_In_Event!!.openInventory.title.split("<===>Player_UUID>>>")[1].split("<===>")[0] == THE_B_SIDE_UUID){

                                                        MC_Version_Detection()
                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"[\",\"font\":\"uniform\",\"color\":\"red\",\"bold\":true,\"italic\":false},{\"text\":\"X\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"] remove friend\",\"font\":\"uniform\",\"color\":\"red\",\"bold\":true,\"italic\":false}]'},SkullOwner:{Id:[I;1590668272,1381189552,-1538418164,-1841266247],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==\"}]}}} 1")

                                                        Player_In_Event!!.openInventory.setItem(10,Player_In_Event!!.enderChest.getItem(0))

                                                    }
                                                }

                                                if (Player_DATA_COM_2["IsOnline"] == "True"){
                                                    if (Bukkit.getPlayer(UUID.fromString(THE_B_SIDE_UUID))!!.openInventory.title.contains("<===>Interaction_Gui<===>")){

                                                        if (Bukkit.getPlayer(UUID.fromString(THE_B_SIDE_UUID))!!.openInventory.title.split("<===>Player_UUID>>>")[1].split("<===>")[0] == Player_In_Event!!.uniqueId.toString()){

                                                            Player_In_Event = Bukkit.getPlayer(UUID.fromString(THE_B_SIDE_UUID))!!
                                                            MC_Version_Detection()
                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"[\",\"font\":\"uniform\",\"color\":\"red\",\"bold\":true,\"italic\":false},{\"text\":\"X\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"] remove friend\",\"font\":\"uniform\",\"color\":\"red\",\"bold\":true,\"italic\":false}]'},SkullOwner:{Id:[I;1590668272,1381189552,-1538418164,-1841266247],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==\"}]}}} 1")

                                                            Player_In_Event!!.openInventory.setItem(10,Player_In_Event!!.enderChest.getItem(0))
                                                            Player_In_Event = cmd_sender
                                                        }
                                                    }
                                                }
                                            }
                                            //==========================================================================================
                                            else{
                                                //send_friend_request
                                                //==================================================================================
                                                var B_SIDE_NOTICE_LIST = ArrayList<String>()

                                                if (!(Player_DATA_CH_2["Channel.Notice"] as ArrayList<String>).isNullOrEmpty()) B_SIDE_NOTICE_LIST = Player_DATA_CH_2["Channel.Notice"] as ArrayList<String>

                                                //VVV B_SIDE_NOTICE_LIST
                                                if (!B_SIDE_NOTICE_LIST.contains("" + Player_In_Event!!.uniqueId.toString() + "_whishes_to_be_your_friend") && !B_SIDE_NOTICE_LIST.contains("" + Player_In_Event!!.uniqueId.toString() + "_whishes_to_be_your_friend_UNREAD")){
                                                    B_SIDE_NOTICE_LIST.add("" + Player_In_Event!!.uniqueId.toString() + "_whishes_to_be_your_friend_UNREAD")
                                                    setYmlValue("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Channel_And_Message.yml","Channel.Notice",B_SIDE_NOTICE_LIST)

                                                    var RECIPIENT_TAGS = ArrayList<String>()
                                                    RECIPIENT_TAGS = Player_DATA_COM_2["Tags"] as ArrayList<String>

                                                    CURRENT_RECIPIENT_UUID = THE_B_SIDE_UUID

                                                    if (Player_DATA_CH_2["Channel.IsInNoticeGUI"] != "True" && Player_DATA_CH_2["Channel.Setting.Channel_5_Notify"] == "Enable"){

                                                        ADD_PLAYER_TAGS(CURRENT_RECIPIENT_UUID,"UNREAD_CHANNEL_5")
                                                    }

                                                    if (!RECIPIENT_TAGS.contains("Disable_Channel")){
                                                        if (Player_DATA_CH_2["Channel.Setting.Channel_5_Notify"] == "Enable"){
                                                            CHAT_BOX_UPDATE(CURRENT_RECIPIENT_UUID)
                                                            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"  chatbox_update $CURRENT_RECIPIENT_UUID force_a78315")
                                                        }
                                                    }
                                                }
                                                //==================================================================================
                                            }

                                        }
                                        else{
                                            Bukkit.dispatchCommand(Player_In_Event!!,"friend remove " + THE_B_SIDE_UUID)
                                        }

                                    }

                                }
                            }

                            //Remove_Friend
                            if (args[0] == "remove"){

                                if (args.size >= 2){
                                    var THE_B_SIDE_UUID = ""
                                    //==========================================================================================
                                    //VVV if arg_1 is UUID
                                    if (File("$main_dir/Player_DATA/" + args[1] + "/Common.yml").exists() ){
                                        THE_B_SIDE_UUID = args[1]
                                    }
                                    else{
                                        //VVV if arg_1 is username
                                        THE_B_SIDE_UUID = Bukkit.getOfflinePlayer("" + args[1]).uniqueId.toString()
                                    }
                                    //==========================================================================================
                                    if (File("$main_dir/Player_DATA/" + THE_B_SIDE_UUID + "/Common.yml").exists() ){

                                        var FRIEND_LIST = ArrayList<String>()
                                        FRIEND_LIST = Player_DATA_FRI_L["Friends"] as ArrayList<String>

                                        if (FRIEND_LIST.contains(THE_B_SIDE_UUID)){
                                            //==================================================================================
                                            //Open_GUI_From_File
                                            Yml_2 = YamlConfiguration.loadConfiguration(File("$main_dir/UI/Double_Confirm.yml"))
                                            menu_gui_1 = Bukkit.createInventory(null,Yml_2["Surface_UI.Size"] as Int,"§0§lUnFriend  §6§l>>>  §2§l" + Bukkit.getOfflinePlayer(UUID.fromString(THE_B_SIDE_UUID)).name + "                                                                                                                                                                                                                                                    <===>Confirmation_GUI_OF_REMOVE_FRIEND<===>___<===>Contact_UUID>>>$THE_B_SIDE_UUID<===>___<===>Locked_slot>>>0_1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24_25_26_27_28_29_30_31_32_33_34_35_36_37_38_39_40_41_42_43_44_45_46_47_48_49_50_51_52_53_54_55_56_57_58_59_60_61_62_63_64_65_66_67_68_69_70_71_72_73_74_75_76_77_78_79_80_81_82_83_84_85_86_87_88_89<===>___<===>Shift_locked_from_player_inv<===>")
                                            var content = (Yml_2["Surface_UI.Contents"] as List<ItemStack?>).toTypedArray()
                                            menu_gui_1.setContents(content)
                                            Player_In_Event!!.openInventory(menu_gui_1)
                                            //==================================================================================

                                        }

                                    }
                                }
                            }

                            //Open_Friend_List
                            if (args[0] == "list"){
                                if (args.size == 1){
                                    if (Player_In_Event!!.scoreboardTags.size > 0){
                                        for (i in 0..Player_In_Event!!.scoreboardTags.size - 1){
                                            if (Player_In_Event!!.scoreboardTags.toList()[i].contains("Friend_Page_at_Last_Time===>>>")){

                                                Bukkit.dispatchCommand(Player_In_Event!!,"friend list " + Player_In_Event!!.scoreboardTags.toList()[i].split("Friend_Page_at_Last_Time===>>>")[1])
                                                return@scheduleSyncDelayedTask
                                            }
                                        }
                                    }
                                }
                                //friend list GUI_Page
                                if (args.size == 2){
                                    if (isInt(args[1])){

                                        //GUI_Page = 1
                                        GUI_Page = args[1].toInt()

                                        //GUI_PAGE
                                        var MY_FRIEND_LIST = ArrayList<String>()
                                        if (!(Player_DATA_FRI_L["Friends"] as ArrayList<String>).isNullOrEmpty()){
                                            MY_FRIEND_LIST = Player_DATA_FRI_L["Friends"] as ArrayList<String>
                                            if ((MY_FRIEND_LIST.lastIndex/42) + 1 < GUI_Page) GUI_Page = (MY_FRIEND_LIST.lastIndex/42) + 1
                                            if (GUI_Page < 1) GUI_Page = 1
                                        }

                                        //Open_GUI_From_File
                                        var LAST_PAGE = "0"
                                        LAST_PAGE = ((MY_FRIEND_LIST.lastIndex/42) + 1).toString()
                                        if (((MY_FRIEND_LIST.lastIndex/42) + 1).toString().startsWith("0")) LAST_PAGE = ((MY_FRIEND_LIST.lastIndex/42) + 1).toString().substring(1)

                                        var menu_gui_X = Bukkit.createInventory(null,54,"§2§lFriends §8($GUI_Page/" + LAST_PAGE + ")                                                                                                                                                                                                                                                     <===>GUI_PAGE>>>$GUI_Page<===>___<===>Friend_List<===>___<===>Locked_slot>>>0_1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24_25_26_27_28_29_30_31_32_33_34_35_36_37_38_39_40_41_42_43_44_45_46_47_48_49_50_51_52_53_54_55_56_57_58_59_60_61_62_63_64_65_66_67_68_69_70_71_72_73_74_75_76_77_78_79_80_81_82_83_84_85_86_87_88_89<===>___<===>Shift_locked_from_player_inv<===>")
                                        Yml_2 = YamlConfiguration.loadConfiguration(File("$main_dir/UI/List_Type_Gui.yml"))
                                        var content = (Yml_2["Surface_UI.Contents"] as List<ItemStack?>).toTypedArray()
                                        menu_gui_X.setContents(content)

                                        //
                                        if (!(Player_DATA_FRI_L["Friends"] as ArrayList<String>).isNullOrEmpty()){
                                            MY_FRIEND_LIST = Player_DATA_FRI_L["Friends"] as ArrayList<String>

                                            XXX_LOOP@for(i in 42*(GUI_Page - 1)..(42*(GUI_Page - 1) + 41)){
                                                // 0 ~ 41
                                                var slot = i - (42 * (i/42))
                                                var ONLINE_STATUS = "Online"
                                                var ONLINE_STATUS_COLOR = "green"
                                                if (Bukkit.getPlayer(UUID.fromString(MY_FRIEND_LIST[i])) == null ){
                                                    ONLINE_STATUS = "Offline"
                                                    ONLINE_STATUS_COLOR = "red"
                                                }
                                                var Player_DATA_X = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/" + MY_FRIEND_LIST[i] + "/Common.yml"))
                                                var HEAD_TEXTURE_VALUE = Player_DATA_X["Head_Texture_Value"]
                                                var PLAYER_NAME = Player_DATA_X["UserName"]


                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'{\"text\":\"$PLAYER_NAME\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}',Lore:['{\"text\":\"=================================\",\"font\":\"uniform\",\"color\":\"gold\",\"italic\":false}','[{\"text\":\"Left-Click \",\"font\":\"uniform\",\"color\":\"gold\",\"italic\":false},{\"text\":\"to Open Interaction Gui\",\"font\":\"uniform\",\"color\":\"dark_green\",\"italic\":false}]','[{\"text\":\"Right-Click \",\"font\":\"uniform\",\"color\":\"gold\",\"italic\":false},{\"text\":\"to UnFriend\",\"font\":\"uniform\",\"color\":\"dark_green\",\"italic\":false}]','{\"text\":\"=================================\",\"font\":\"uniform\",\"color\":\"gold\",\"italic\":false}','{\"text\":\"$ONLINE_STATUS\",\"font\":\"uniform\",\"color\":\"$ONLINE_STATUS_COLOR\",\"bold\":true,\"italic\":false}']},SkullOwner:{Id:[I;853408908,-902150958,-1544141899,-531255144],Properties:{textures:[{Value:\"$HEAD_TEXTURE_VALUE\"}]}}} 1")

                                                var ITEMSTACK_X = Player_In_Event!!.enderChest.getItem(0)!!
                                                var ITEMSTACK_mdFF = (ITEMSTACK_X.itemMeta) as SkullMeta

                                                //ITEMSTACK_mdFF.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(MY_FRIEND_LIST[i])))
                                                ITEMSTACK_mdFF.setLocalizedName("UUID===>>>" + MY_FRIEND_LIST[i])

                                                ITEMSTACK_X.setItemMeta(ITEMSTACK_mdFF)

                                                menu_gui_X.setItem((slot + 2* (slot/7)),ITEMSTACK_X)

                                                if (i >= MY_FRIEND_LIST.lastIndex) break@XXX_LOOP
                                            }

                                        }
                                        Player_In_Event!!.openInventory(menu_gui_X)

                                    }
                                }

                            }
                            
                        }, 1L)

                    }

                }
                //Party_System

                "party"-> {
                    Player_In_Event = cmd_sender
                    Player_UUID = Player_In_Event!!.uniqueId.toString()
                    MC_Version_Detection()

                    if (args.isEmpty()){

                        if (!Player_In_Event!!.scoreboardTags.contains("IS_IN_PARTY_MENU_LAST_TIME")){
                            Open_Party_Finder()
                        }else{
                            //Open Party_Menu
                            Open_Party_Menu()
                        }
                        return true
                    }
                    if (args.size >= 2){
                        if (args[0] == "invite" || args[0] == "join"){
                            Bukkit.getServer().scheduler.scheduleSyncDelayedTask(this,Runnable {

                                var THE_B_SIDE_UUID = ""
                                var A_SIDE_PLAYER_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))
                                //==========================================================================================
                                //VVV if arg_1 is UUID
                                if (File("$main_dir/Player_DATA/" + args[1] + "/Common.yml").exists() ){
                                    THE_B_SIDE_UUID = args[1]
                                }
                                else{
                                    //VVV if arg_1 is username
                                    THE_B_SIDE_UUID = Bukkit.getOfflinePlayer("" + args[1]).uniqueId.toString()
                                }
                                //==========================================================================================
                                if (THE_B_SIDE_UUID != ""){
                                    var B_SIDE_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Party.yml"))

                                    if (args[0] == "invite"){

                                        if (A_SIDE_PLAYER_DATA_PARTY["Party"] != ""){
                                            var A_SIDE_PARTY_DATA = YamlConfiguration.loadConfiguration(File("$main_dir/Party/Party_DATA/" + A_SIDE_PLAYER_DATA_PARTY["Party"] + ".yml"))

                                            if (A_SIDE_PARTY_DATA["Leader"] == Player_UUID){
                                                if (B_SIDE_DATA_PARTY["Party"] == ""){

                                                    var B_SIDE_PLAYER_DATE_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Channel_And_Message.yml"))
                                                    var B_SIDE_PLAYER_DATE_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Channel_And_Message.yml"))

                                                    //send_Party_invitation
                                                    //==================================================================================
                                                    var B_SIDE_NOTICE_LIST = ArrayList<String>()

                                                    if (!(B_SIDE_PLAYER_DATE_CH["Channel.Notice"] as ArrayList<String>).isNullOrEmpty()) B_SIDE_NOTICE_LIST = B_SIDE_PLAYER_DATE_CH["Channel.Notice"] as ArrayList<String>

                                                    //VVV B_SIDE_NOTICE_LIST
                                                    if (!B_SIDE_NOTICE_LIST.contains("" + Player_In_Event!!.uniqueId.toString() + "_whishes_to_invite_you_to_join_the_party") && !B_SIDE_NOTICE_LIST.contains("" + Player_In_Event!!.uniqueId.toString() + "_whishes_to_invite_you_to_join_the_party_UNREAD")){
                                                        B_SIDE_NOTICE_LIST.add("" + Player_In_Event!!.uniqueId.toString() + "_whishes_to_invite_you_to_join_the_party_UNREAD")
                                                        setYmlValue("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Channel_And_Message.yml","Channel.Notice",B_SIDE_NOTICE_LIST)

                                                        var RECIPIENT_TAGS = ArrayList<String>()
                                                        RECIPIENT_TAGS = B_SIDE_PLAYER_DATE_COM["Tags"] as ArrayList<String>

                                                        CURRENT_RECIPIENT_UUID = THE_B_SIDE_UUID

                                                        if (B_SIDE_PLAYER_DATE_CH["Channel.IsInNoticeGUI"] != "True" && B_SIDE_PLAYER_DATE_CH["Channel.Setting.Channel_5_Notify"] == "Enable"){

                                                            ADD_PLAYER_TAGS(CURRENT_RECIPIENT_UUID,"UNREAD_CHANNEL_5")
                                                        }

                                                        if (!RECIPIENT_TAGS.contains("Disable_Channel")){
                                                            if (B_SIDE_PLAYER_DATE_CH["Channel.Setting.Channel_5_Notify"] == "Enable"){
                                                                CHAT_BOX_UPDATE(CURRENT_RECIPIENT_UUID)
                                                                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"  chatbox_update $CURRENT_RECIPIENT_UUID force_a78315")
                                                            }
                                                        }
                                                    }
                                                    //==================================================================================
                                                }
                                            }
                                        }

                                    }
                                    if (args[0] == "join"){
                                        if (A_SIDE_PLAYER_DATA_PARTY["Party"] == ""){

                                            if (B_SIDE_DATA_PARTY["Party"] != ""){
                                                var B_SIDE_PARTY_DATA = YamlConfiguration.loadConfiguration(File("$main_dir/Party/Party_DATA/" + B_SIDE_DATA_PARTY["Party"] + ".yml"))

                                                if (B_SIDE_PARTY_DATA["Leader"] == THE_B_SIDE_UUID){
                                                    var B_SIDE_PLAYER_DATE_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Channel_And_Message.yml"))
                                                    var B_SIDE_PLAYER_DATE_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Channel_And_Message.yml"))

                                                    //send_Party_joining_request
                                                    //==================================================================================
                                                    var B_SIDE_NOTICE_LIST = ArrayList<String>()

                                                    if (!(B_SIDE_PLAYER_DATE_CH["Channel.Notice"] as ArrayList<String>).isNullOrEmpty()) B_SIDE_NOTICE_LIST = B_SIDE_PLAYER_DATE_CH["Channel.Notice"] as ArrayList<String>

                                                    //VVV B_SIDE_NOTICE_LIST
                                                    if (!B_SIDE_NOTICE_LIST.contains("" + Player_In_Event!!.uniqueId.toString() + "_whishes_to_join_your_party") && !B_SIDE_NOTICE_LIST.contains("" + Player_In_Event!!.uniqueId.toString() + "_whishes_to_join_your_party_UNREAD")){
                                                        B_SIDE_NOTICE_LIST.add("" + Player_In_Event!!.uniqueId.toString() + "_whishes_to_join_your_party_UNREAD")
                                                        setYmlValue("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Channel_And_Message.yml","Channel.Notice",B_SIDE_NOTICE_LIST)

                                                        var RECIPIENT_TAGS = ArrayList<String>()
                                                        RECIPIENT_TAGS = B_SIDE_PLAYER_DATE_COM["Tags"] as ArrayList<String>

                                                        CURRENT_RECIPIENT_UUID = THE_B_SIDE_UUID

                                                        if (B_SIDE_PLAYER_DATE_CH["Channel.IsInNoticeGUI"] != "True" && B_SIDE_PLAYER_DATE_CH["Channel.Setting.Channel_5_Notify"] == "Enable"){

                                                            ADD_PLAYER_TAGS(CURRENT_RECIPIENT_UUID,"UNREAD_CHANNEL_5")
                                                        }

                                                        if (!RECIPIENT_TAGS.contains("Disable_Channel")){
                                                            if (B_SIDE_PLAYER_DATE_CH["Channel.Setting.Channel_5_Notify"] == "Enable"){
                                                                CHAT_BOX_UPDATE(CURRENT_RECIPIENT_UUID)
                                                                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"  chatbox_update $CURRENT_RECIPIENT_UUID force_a78315")
                                                            }
                                                        }
                                                    }
                                                    //==================================================================================
                                                }
                                            }
                                        }

                                    }

                                }


                            }, 0L)

                        }
                    }
                }
                "gmkt" -> {
                    if (args.isEmpty()) return true
                    Player_In_Event = cmd_sender
                    MC_Version_Detection()
                    GUI_Page = 1
                    GUI_TITLE_FOR_FUNCTION = "Global_Market"

                }
            }
        }
        return true
    }

    //if command is unknown
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        val msg = e.message
        val args = msg.split(" ")[0]
        //val p = e.player
        var Yml_plugin = YamlConfiguration.loadConfiguration(File("$main_dir/plugin.yml"))
        var CMD_LIST = Yml_plugin.getConfigurationSection("commands")!!.getKeys(false).toList()

        if ((Bukkit.getServer().helpMap.getHelpTopic(args) == null && !CMD_LIST.contains(args.split("/")[1])) || args == "/w" || args == "/W" ) {

            e.isCancelled = true
            //p.sendMessage("§cThis command does not exist.")
        }
    }
//######################################################################################################################
//######################################################################################################################
    //@PacketHandler
    //fun Packet_REC(e: PacketPlayReceiveEvent){

    //}
    //fun Packet_SEND(e: PacketPlaySendEvent){

    //}
//######################################################################################################################
//######################################################################################################################
//Version_Detection
    fun MC_Version_Detection(){
        MC_VERSION = Bukkit.getBukkitVersion().split("_","-",".")[1]
        if (Bukkit.getBukkitVersion().startsWith("1.")){
            if (MC_VERSION.toInt() <= 16){
                command_for_set_item_in_gui = "replaceitem entity " + Player_In_Event!!.name + " enderchest.0 "
            }else{
                command_for_set_item_in_gui = "execute as " + Player_In_Event!!.name + " run item replace entity @s enderchest.0 with "
            }
        }
        if (Bukkit.getBukkitVersion().startsWith("2.")){
            command_for_set_item_in_gui = "item replace entity " + Player_In_Event!!.name + " enderchest.0 with "
        }
    }
//######################################################################################################################
//######################################################################################################################
//Detection of Numeric type
    fun isInt(InPut: String): Boolean {
        if (!InPut.contains(".")){
            try {
                InPut.toInt()
            } catch (EX: NumberFormatException) {
                return false
            }
            return true
        }
        else{
            return false
        }
    }
//######################################################################################################################
    fun getUuid(name: String): String? {
        val url = "https://api.mojang.com/users/profiles/minecraft/$name"
        try {
            val UUIDJson: String = IOUtils.toString(URL(url))
            if (UUIDJson.isEmpty()) return "invalid name"
            val UUIDObject = JSONValue.parseWithException(UUIDJson) as JSONObject
            return UUIDObject["id"].toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
    }
    return "error"
}
//######################################################################################################################
//######################################################################################################################
//PLAYER_TAG_IN_PLAYER_DATA
    fun GET_PLAYER_TAGS(Player_UUID_String: String){
        Player_DATA_TAGS = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID_String/Common.yml"))
        if (Player_DATA_TAGS["UserName"] != null){
            Player_TAGS = Player_DATA_TAGS["Tags"] as ArrayList<String>
        }
    }
    fun REMOVE_PLAYER_TAGS(Player_UUID_String: String , TAGS: String){
        Player_DATA_TAGS = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID_String/Common.yml"))
        if (Player_DATA_TAGS["UserName"] != null){
            Player_TAGS = Player_DATA_TAGS["Tags"] as ArrayList<String>
            if (Player_TAGS.contains("" + TAGS)){
                Player_TAGS.remove("" + TAGS)
                setYmlValue("$main_dir/Player_DATA/$Player_UUID_String/Common.yml","Tags",Player_TAGS)
            }
        }
    }
    fun ADD_PLAYER_TAGS(Player_UUID_String: String , TAGS: String){
        Player_DATA_TAGS = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID_String/Common.yml"))
        if (Player_DATA_TAGS["UserName"] != null){

            Player_TAGS = ArrayList<String>()
            Player_TAGS = Player_DATA_TAGS["Tags"] as ArrayList<String>

            if (!Player_TAGS.contains("" + TAGS)){
                Player_TAGS.add("" + TAGS)
                setYmlValue("$main_dir/Player_DATA/$Player_UUID_String/Common.yml","Tags",Player_TAGS)

            }
        }
    }
//######################################################################################################################
//######################################################################################################################
//When_Join_The_Game
    @EventHandler
    fun onJoin(e: PlayerJoinEvent){
        e.setJoinMessage("")

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, {
            Player_In_Event = e.player
            Player_UUID = e.player.uniqueId.toString()
            CURRENT_RECIPIENT_NAME = e.player.name
            CURRENT_RECIPIENT_UUID = Player_In_Event!!.uniqueId.toString()
            if (!File("$main_dir/Player_DATA/$Player_UUID/Common.yml").exists()) File("$main_dir/Player_DATA/$Player_UUID").mkdirs()
            var Player_DATA_X = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Common.yml"))

            setYmlValue("$main_dir/Player_DATA/$Player_UUID/Common.yml","IsOnline","False")
            if (Player_DATA_X["UserName"] == null) setYmlValue("$main_dir/Player_DATA/$Player_UUID/Common.yml","UserName",e.player.name)
            setYmlValue("$main_dir/Player_DATA/$Player_UUID/Common.yml","IsOnline","True")
            if (Player_DATA_X["Tags"] == null)setYmlValue("$main_dir/Player_DATA/$Player_UUID/Common.yml","Tags",ArrayList<String>())


            Player_DATA_X = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))
            if (Player_DATA_X["Party"] == null)setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Party","")
            if (Player_DATA_X["String_Filter"] == null)setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","String_Filter","")
            if (Player_DATA_X["Level_Filter"] == null)setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Level_Filter",0)
            if (Player_DATA_X["Party_Type_Filter"] == null)setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Party_Type_Filter",0)
            if (Player_DATA_X["Show_Full_Parties"] == null)setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Show_Full_Parties",true)


            Player_DATA_X = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Friends_List.yml"))
            if (Player_DATA_X["Friends"] == null)setYmlValue("$main_dir/Player_DATA/$Player_UUID/Friends_List.yml","Friends",ArrayList<String>())


            Player_DATA_X = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml"))

            if (Player_DATA_X["Channel.Notice"] == null) setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Notice",ArrayList<String>())

            if (Player_DATA_X["SystemMessage.Strings"] == null)setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","SystemMessage.Strings",ArrayList<String>())

            if (Player_DATA_X["Channel.CurrentChannel"] == null){
                setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.CurrentChannel","0")
            }
            if (Player_DATA_X["Channel.Setting"] == null){
                for (n in 0..5){
                    setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_" + n + "_Notify","Enable")
                    setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_" + n + "_Play_Sound","Enable")
                }
            }
            if (!IOUtils.toString(URL("https://sessionserver.mojang.com/session/minecraft/profile/" + Player_UUID )).isNullOrEmpty()){
                var HEAD_TEXTURE_VALUE = IOUtils.toString(URL("https://sessionserver.mojang.com/session/minecraft/profile/" + Player_UUID )).split("\"value\" :")[1].split("\"")[1]
                setYmlValue("$main_dir/Player_DATA/$Player_UUID/Common.yml","Head_Texture_Value",HEAD_TEXTURE_VALUE)
            }

            CHAT_BOX_UPDATE(Player_UUID) }, 1L)

    }
//######################################################################################################################
//######################################################################################################################
//When_Leave_The_Game
    @EventHandler
    fun onLeave(e: PlayerQuitEvent){
        e.setQuitMessage("")
        Player_UUID = e.player.uniqueId.toString()
        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Common.yml","IsOnline","False")
    }
//######################################################################################################################
//######################################################################################################################
//Save GUI_Surface
    @EventHandler
    fun onInventoryClose_ACC(e: InventoryCloseEvent){
        if (e.view.title.contains("<===>GUI_EDIT<===>")){

            File_Path_1 = "$main_dir/UI/" + e.view.title.split("<===>GUI_FILE_NAME>>>")[1].split("<===>")[0]
            Yml_1 = YamlConfiguration.loadConfiguration(File(File_Path_1))

            setYmlValue(File_Path_1,"Surface_UI.Contents",e.view.topInventory.contents)
            setYmlValue(File_Path_1,"Surface_UI.Size",e.view.topInventory.size)

        }
    }
//######################################################################################################################
//######################################################################################################################
//GUI_RUNNING
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryClick_ASD(e: InventoryClickEvent){
        if (e.view.title.contains("<===>Shift_Lock_From_Inv<===>")){
            if (e.isShiftClick && e.rawSlot >= e.view.topInventory.size){
                e.isCancelled = true
            }
        }
        if (e.view.title.contains("<===>Locked_slot>>>")){
            var List_Of_Locked_Slot = e.view.title.split("<===>Locked_slot>>>")[1].split("<===>")[0].split("_")

            for (i in 0..List_Of_Locked_Slot.lastIndex){
                val newThread = Thread {
                    if (e.rawSlot == List_Of_Locked_Slot[i].toInt()){
                        e.isCancelled = true
                    }
                }
                newThread.start()
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryDrag_ASD(e: InventoryDragEvent){
        var FGG = e.whoClicked.inventory.contents
        if (e.view.title.contains("<===>Locked_slot>>>")){
            var List_Of_Locked_Slot = e.view.title.split("<===>Locked_slot>>>")[1].split("<===>")[0].split("_")

            for(i in 0 ..e.rawSlots.size-1){
                val newThread = Thread{
                    if (List_Of_Locked_Slot.contains(e.rawSlots.elementAt(i).toString())){
                        e.isCancelled = true
                        //e.whoClicked.inventory.setContents(FGG)
                    }
                }
                newThread.start()
            }
        }
    }
//######################################################################################################################
//######################################################################################################################
//Open_Party_Finder
    fun Open_Party_Finder(){
        var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))

        Player_In_Event!!.removeScoreboardTag("IS_IN_PARTY_MENU_LAST_TIME")
        //GUI_PAGE
        var THE_GUI_PAGE = 1
        var LAST_PAGE = "1"
        var had_ever_Opened = false
        for (i in 0..Player_In_Event!!.scoreboardTags.size - 1){
            if (Player_In_Event!!.scoreboardTags.toList()[i].contains("Party_Finder_Page_at_Last_Time===>>>")){
                had_ever_Opened = true
                THE_GUI_PAGE = Player_In_Event!!.scoreboardTags.toList()[i].split("Party_Finder_Page_at_Last_Time===>>>")[1].toInt()
            }
        }
        if (had_ever_Opened == false){
            THE_GUI_PAGE = 1
            Player_In_Event!!.scoreboardTags.add("Party_Finder_Page_at_Last_Time===>>>1")
        }

        var List_Of_Party_Finder = ArrayList<String>()
        var YML_List_Of_Party_Finder = YamlConfiguration.loadConfiguration(File("$main_dir/Party/List_Of_Party_Finder.yml"))
        if (!(YML_List_Of_Party_Finder["Party_List"] as ArrayList<String>).isNullOrEmpty()){
            List_Of_Party_Finder = YML_List_Of_Party_Finder["Party_List"] as ArrayList<String>

        }

        var THE_PT_FILTER_LIST = ArrayList<String>()
        if (!List_Of_Party_Finder.isNullOrEmpty()){
            //===>
            THE_PT_FILTER_LIST = List_Of_Party_Finder.filter {
                (it.split("<===>Description===>>>")[1].split("<===>")[0].contains("" + Player_DATA_PARTY["String_Filter"]) || it.split("<===>Leader===>>>")[1].split("<===>")[0].contains("" + Player_DATA_PARTY["String_Filter"])) &&
                        it.split("<===>Min_Combat_Level===>>>")[1].split("<===>")[0].toInt() >= Player_DATA_PARTY["Level_Filter"] as Int &&
                        (it.split("<===>Party_Type===>>>")[1].split("<===>")[0].toInt() + 1 == Player_DATA_PARTY["Party_Type_Filter"] as Int ||
                                it.split("<===>Party_Type===>>>")[1].split("<===>")[0].toInt() - Player_DATA_PARTY["Party_Type_Filter"] as Int == it.split("<===>Party_Type===>>>")[1].split("<===>")[0].toInt())
            } as ArrayList<String>

            if (Player_DATA_PARTY["Show_Full_Parties"] == false){

                THE_PT_FILTER_LIST = THE_PT_FILTER_LIST.filter {
                    it.split("<===>Current_Players===>>>")[1].split("<===>")[0].toInt() < it.split("<===>Max_Player===>>>")[1].split("<===>")[0].toInt()
                } as ArrayList<String>
            }

            //===>
            if (!THE_PT_FILTER_LIST.isNullOrEmpty()){
                //GUI_PAGE
                if ((THE_PT_FILTER_LIST.lastIndex/30) + 1 < THE_GUI_PAGE) THE_GUI_PAGE = (THE_PT_FILTER_LIST.lastIndex/30) + 1
                if (THE_GUI_PAGE < 1) THE_GUI_PAGE = 1

                //LAST_PAGE
                LAST_PAGE = ((THE_PT_FILTER_LIST.lastIndex/30) + 1).toString()
                if (((THE_PT_FILTER_LIST.lastIndex/30) + 1).toString().startsWith("0")) LAST_PAGE = ((THE_PT_FILTER_LIST.lastIndex/30) + 1).toString().substring(1)

            }
        }

        //Open_GUI_From_File
        var menu_gui_X = Bukkit.createInventory(null,54,"§2§lParty Finder §8($THE_GUI_PAGE/" + LAST_PAGE + ")                                                                                                                                                                                                                                                     <===>GUI_PAGE>>>$THE_GUI_PAGE<===>___<===>Party_Finder<===>___<===>Locked_slot>>>0_1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24_25_26_27_28_29_30_31_32_33_34_35_36_37_38_39_40_41_42_43_44_45_46_47_48_49_50_51_52_53_54_55_56_57_58_59_60_61_62_63_64_65_66_67_68_69_70_71_72_73_74_75_76_77_78_79_80_81_82_83_84_85_86_87_88_89<===>___<===>Shift_locked_from_player_inv<===>")
        var Yml_PF = YamlConfiguration.loadConfiguration(File("$main_dir/UI/Party_Finder.yml"))
        var content = (Yml_PF["Surface_UI.Contents"] as List<ItemStack?>).toTypedArray()
        menu_gui_X.setContents(content)
        //
        if (Player_DATA_PARTY["Show_Full_Parties"] == false){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'{\"text\":\"Show Full\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}',Lore:['[{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','{\"text\":\"Display full parties\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"on the list\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','[{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Current Value \",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false},{\"text\":\"ᐁ\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}]','{\"text\":\"No\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}','[{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','{\"text\":\"Click to switch\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\" \"}']},SkullOwner:{Id:[I;-1477925756,-683981452,-1613257316,1561705494],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNjYjhmNjFlN2Y2YmY5NTdhMjEzNGU5NmZhZWIwZmM5MjQxMzdkNGJmZjg4ZDk1MThiMmJmNjYyNTg2YzkyZSJ9fX0=\"}]}}} 1")
        }else{
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'{\"text\":\"Show Full\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}',Lore:['[{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','{\"text\":\"Display full parties\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"on the list\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','[{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Current Value \",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false},{\"text\":\"ᐁ\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}]','{\"text\":\"Yes\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}','[{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','{\"text\":\"Click to switch\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\" \"}']},SkullOwner:{Id:[I;1814462106,1919503531,-1971002049,-1440026433],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmNiZWE4N2MzZmMxMmU5NTk5MTgxYjk3ZWE1Zjc5NmY4MDIwMjQ1YmFiM2IwZjVmZjMyNDljMzA3YjM3ZWZjOCJ9fX0=\"}]}}} 1")
        }
        ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
        menu_gui_X.setItem(36,ITEMSTACK)


        if (!THE_PT_FILTER_LIST.isNullOrEmpty()){

            VVV_LOOP@for(k in 30*(THE_GUI_PAGE - 1)..(30*(THE_GUI_PAGE - 1) + 29)){
                // 0 ~ 29
                var slot = k - (30 * (k/30))

                var ITEM_MC_ID = ""
                var Party_Type_Name = ""

                if (THE_PT_FILTER_LIST[k].split("<===>Party_Type===>>>")[1].split("<===>")[0].toInt() == 0 ){ ITEM_MC_ID = "amethyst_shard" ; Party_Type_Name = "Grinding Mobs"
                }
                if (THE_PT_FILTER_LIST[k].split("<===>Party_Type===>>>")[1].split("<===>")[0].toInt() == 1 ){ ITEM_MC_ID = "trident" ; Party_Type_Name = "Fighting Boss"
                }
                if (THE_PT_FILTER_LIST[k].split("<===>Party_Type===>>>")[1].split("<===>")[0].toInt() == 2 ){ ITEM_MC_ID = "mossy_stone_bricks" ; Party_Type_Name = "Dungeon Challenge"
                }
                if (THE_PT_FILTER_LIST[k].split("<===>Party_Type===>>>")[1].split("<===>")[0].toInt() == 3 ){ ITEM_MC_ID = "writable_book" ; Party_Type_Name = "Quest"
                }
                if (THE_PT_FILTER_LIST[k].split("<===>Party_Type===>>>")[1].split("<===>")[0].toInt() == 4 ){ ITEM_MC_ID = "red_tulip" ; Party_Type_Name = "Social"
                }
                if (THE_PT_FILTER_LIST[k].split("<===>Party_Type===>>>")[1].split("<===>")[0].toInt() == 5 ){ ITEM_MC_ID = "name_tag" ; Party_Type_Name = "Others"
                }

                var Desc = THE_PT_FILTER_LIST[k].split("<===>Description===>>>")[1].split("<===>")[0]
                var Desc_List = ArrayList<String>()
                var Desc_IN_cmd = ""
                var Limit_Length = 36
                var TEMP_WIDTH_PER_TEXT = 0.0

                var PROCESS_TEMP_String = ""

                var HEAD_TEMP_String = ""
                var HEAD_TEMP_String_Width = 0.0
                var PROCESS_TEMP_String_Width = 0.0

                for(i in Desc.indices){
                    if (Desc.substring(i,i+1).toByteArray().size == 1 || Desc.substring(i,i+1) == " "){
                        TEMP_WIDTH_PER_TEXT = 1.5
                    }else{
                        TEMP_WIDTH_PER_TEXT = Desc.substring(i,i+1).toByteArray().size.toDouble()
                    }
                    PROCESS_TEMP_String_Width += TEMP_WIDTH_PER_TEXT
                    PROCESS_TEMP_String += Desc.substring(i,i+1)
                    if (HEAD_TEMP_String_Width + PROCESS_TEMP_String_Width <= Limit_Length){

                        if (Desc.substring(i,i+1) == " "){
                            //"AAAA 我我我我 BBBB 你你你你你A CCCCCCCCC DDDDDDDDDDDDDDDDDDDDD12345"
                            HEAD_TEMP_String = HEAD_TEMP_String + PROCESS_TEMP_String //ABC
                            HEAD_TEMP_String_Width = HEAD_TEMP_String_Width + PROCESS_TEMP_String_Width - 1.5
                            PROCESS_TEMP_String_Width = 1.5
                            PROCESS_TEMP_String = ""
                        }else{
                            if (i == Desc.lastIndex){
                                Desc_List.add(PROCESS_TEMP_String.trim())
                            }
                        }

                    }else{
                        if (HEAD_TEMP_String_Width == 0.0){

                            PROCESS_TEMP_String_Width = TEMP_WIDTH_PER_TEXT
                            Desc_List.add(PROCESS_TEMP_String.dropLast(1))
                            PROCESS_TEMP_String = PROCESS_TEMP_String.takeLast(1)

                        }else{
                            Desc_List.add(HEAD_TEMP_String.trim())
                            HEAD_TEMP_String = ""
                            HEAD_TEMP_String_Width = 0.0
                        }
                    }
                }

                if (Desc == ""){
                    Desc_List.add("None")
                }

                for(s in Desc_List.indices){
                    Desc_IN_cmd = Desc_IN_cmd + "'{\"text\":\"" + Desc_List[s] + "\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}',"
                }

                var LOOPED_PARTY_DATA = YamlConfiguration.loadConfiguration(File("$main_dir/Party/Party_DATA/" + THE_PT_FILTER_LIST[k].split("<===>Party_UUID===>>>")[1].split("<===>")[0] + ".yml"))

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "" + ITEM_MC_ID + "{display:{Name:'{\"text\":\"" + THE_PT_FILTER_LIST[k].split("<===>Leader===>>>")[1].split("<===>")[0] + "\\'s Party\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":true,\"italic\":false}',Lore:['[{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Descripton \",\"font\":\"uniform\",\"color\":\"#1189BD\",\"bold\":true,\"italic\":false},{\"text\":\"ᐁ\",\"font\":\"uniform\",\"color\":\"#26C9BE\",\"bold\":true,\"italic\":false}]'," + Desc_IN_cmd + "'{\"text\":\" \"}','[{\"text\":\"Leader: \",\"font\":\"uniform\",\"color\":\"dark_aqua\",\"bold\":true,\"italic\":false},{\"text\":\"" + THE_PT_FILTER_LIST[k].split("<===>Leader===>>>")[1].split("<===>")[0] + "\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Type: \",\"font\":\"uniform\",\"color\":\"dark_aqua\",\"bold\":true,\"italic\":false},{\"text\":\"" + Party_Type_Name + "\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}]','{\"text\":\" \"}','[{\"text\":\"✔ \",\"font\":\"uniform\",\"color\":\"#1189BD\",\"bold\":false,\"italic\":false},{\"text\":\"Current Players: \",\"font\":\"uniform\",\"color\":\"#66B7BA\",\"bold\":true,\"italic\":false},{\"text\":\"" + THE_PT_FILTER_LIST[k].split("<===>Current_Players===>>>")[1].split("<===>")[0] + "/" + THE_PT_FILTER_LIST[k].split("<===>Max_Player===>>>")[1].split("<===>")[0] + "\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}]','[{\"text\":\"✗\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\" \",\"font\":\"uniform\",\"bold\":true,\"italic\":false},{\"text\":\"Combat Lv.min: \",\"font\":\"uniform\",\"color\":\"#66B7BA\",\"bold\":true,\"italic\":false},{\"text\":\"" + THE_PT_FILTER_LIST[k].split("<===>Min_Combat_Level===>>>")[1].split("<===>")[0] + "\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}]','{\"text\":\" \"}','[{\"text\":\"➤ \",\"font\":\"uniform\",\"color\":\"dark_aqua\",\"bold\":false,\"italic\":false},{\"text\":\"Click here to apply\",\"font\":\"uniform\",\"color\":\"#74B5C2\",\"bold\":true,\"italic\":false}]','{\"text\":\" \"}']}} 1")
                var ITEMSTACK_X = Player_In_Event!!.enderChest.getItem(0)!!

                //var ITEMSTACK_X = Player_In_Event!!.enderChest.getItem(0)!!
                var ITEMSTACK_mdFF = ITEMSTACK_X.itemMeta
                ITEMSTACK_mdFF!!.setLocalizedName("Party_UUID===>>>" + THE_PT_FILTER_LIST[k].split("<===>Party_UUID===>>>")[1].split("<===>")[0])

                ITEMSTACK_X.setItemMeta(ITEMSTACK_mdFF)

                menu_gui_X.setItem(2 + slot + ((slot/5)*4),ITEMSTACK_X)

                if (k >= THE_PT_FILTER_LIST.lastIndex) break@VVV_LOOP
            }
        }

        THE_Player_Who_IS_Viewing_Party_Finder.add(Player_UUID)

        if (Player_DATA_PARTY["Party"] != ""){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'{\"text\":\"Back to Party Menu\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}'},SkullOwner:{Id:[I;1159280686,-1989590109,-1834888670,-1988334781],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZjYmFlNzI0NmNjMmM2ZTg4ODU4NzE5OGM3OTU5OTc5NjY2YjRmNWE0MDg4ZjI0ZTI2ZTA3NWYxNDBhZTZjMyJ9fX0=\"}]}}} 1")
            ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
            menu_gui_X.setItem(8,ITEMSTACK)
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "spruce_sign{display:{Name:'{\"text\":\"String Filter\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}',Lore:['[{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','{\"text\":\"Filter based on the party\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"owner\\'s name and\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"description\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','[{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Current Value \",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false},{\"text\":\"ᐁ\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}]','{\"text\":\"" + Player_DATA_PARTY["String_Filter"] + "\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}','[{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Left-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to set value\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Right-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to reset\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','{\"text\":\" \"}']},BlockEntityTag:{}} 1")
        ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
        menu_gui_X.setItem(9,ITEMSTACK)

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "experience_bottle{display:{Name:'{\"text\":\"Level Filter\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}',Lore:['[{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','{\"text\":\"Filter based on  \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"the minimum combat level\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"required for joining parties\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','[{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Current Value \",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false},{\"text\":\"ᐁ\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}]','{\"text\":\"" + Player_DATA_PARTY["Level_Filter"] + "\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}','[{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Left-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to increase by 1\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Right-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to decrease by 1\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','{\"text\":\" \"}','[{\"text\":\"Holding \",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false},{\"text\":\"Shift\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":true,\"italic\":false},{\"text\":\" while Clicking\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','{\"text\":\"to increase/decrease by 10\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}','{\"text\":\" \"}']}} 1")
        ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
        menu_gui_X.setItem(18,ITEMSTACK)

        var TIT_0_ICON = "-" ;var TIT_1_ICON = "-" ;var TIT_2_ICON = "-" ;var TIT_3_ICON = "-" ;var TIT_4_ICON = "-" ;var TIT_5_ICON = "-" ;var TIT_6_ICON = "-" ;
        var TIT_0_COLOR = "#AD976F" ; var TIT_1_COLOR = "#AD976F" ; var TIT_2_COLOR = "#AD976F" ; var TIT_3_COLOR = "#AD976F" ; var TIT_4_COLOR = "#AD976F" ; var TIT_5_COLOR = "#AD976F" ; var TIT_6_COLOR = "#AD976F" ;
        var SUB_0_COLOR = "#AD976F" ; var SUB_1_COLOR = "#AD976F" ; var SUB_2_COLOR = "#AD976F" ; var SUB_3_COLOR = "#AD976F" ; var SUB_4_COLOR = "#AD976F" ; var SUB_5_COLOR = "#AD976F" ; var SUB_6_COLOR = "#AD976F" ;

        if (Player_DATA_PARTY["Party_Type_Filter"] == 0 ){ TIT_0_ICON = "ᐉ" ; TIT_0_COLOR = "gold" ; SUB_0_COLOR = "dark_purple"
        }
        if (Player_DATA_PARTY["Party_Type_Filter"] == 1 ){ TIT_1_ICON = "ᐉ" ; TIT_1_COLOR = "gold" ; SUB_1_COLOR = "dark_purple"
        }
        if (Player_DATA_PARTY["Party_Type_Filter"] == 2 ){ TIT_2_ICON = "ᐉ" ; TIT_2_COLOR = "gold" ; SUB_2_COLOR = "dark_purple"
        }
        if (Player_DATA_PARTY["Party_Type_Filter"] == 3 ){ TIT_3_ICON = "ᐉ" ; TIT_3_COLOR = "gold" ; SUB_3_COLOR = "dark_purple"
        }
        if (Player_DATA_PARTY["Party_Type_Filter"] == 4 ){ TIT_4_ICON = "ᐉ" ; TIT_4_COLOR = "gold" ; SUB_4_COLOR = "dark_purple"
        }
        if (Player_DATA_PARTY["Party_Type_Filter"] == 5 ){ TIT_5_ICON = "ᐉ" ; TIT_5_COLOR = "gold" ; SUB_5_COLOR = "dark_purple"
        }
        if (Player_DATA_PARTY["Party_Type_Filter"] == 6 ){ TIT_6_ICON = "ᐉ" ; TIT_6_COLOR = "gold" ; SUB_6_COLOR = "dark_purple"
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'{\"text\":\"Party Type\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}',Lore:['[{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#1189BD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#227DBC\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#3370BB\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#4464BA\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#5657BA\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#674BB9\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#783EB8\",\"bold\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#8932B7\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#984EBE\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#A86AC4\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#B786CB\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#C6A1D2\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#D5BDD9\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#E5D9DF\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#F4F5E6\",\"bold\":false,\"italic\":false}]','{\"text\":\"Filter parties based on its type\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#1189BD\",\"bold\":false,\"italic\":false}','[{\"text\":\"" + TIT_0_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_0_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Any\",\"font\":\"uniform\",\"color\":\"" + SUB_0_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_1_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_1_COLOR+ "\",\"bold\":false,\"italic\":false},{\"text\":\"Grinding Mobs\",\"font\":\"uniform\",\"color\":\"" + SUB_1_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_2_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_2_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Fighting Boss\",\"font\":\"uniform\",\"color\":\"" + SUB_2_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_3_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_3_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Dungeon Challenge\",\"font\":\"uniform\",\"color\":\"" + SUB_3_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_4_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_4_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Quest\",\"font\":\"uniform\",\"color\":\"" + SUB_4_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_5_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_5_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Social\",\"font\":\"uniform\",\"color\":\"" + SUB_5_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_6_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_6_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Others\",\"font\":\"uniform\",\"color\":\"" + SUB_6_COLOR + "\",\"bold\":false,\"italic\":false}]','{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3370BB\",\"bold\":false,\"italic\":false}','[{\"text\":\"Left-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to go down\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Right-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to go up\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','{\"text\":\" \"}']},SkullOwner:{Id:[I;968138172,-1748742378,-2026870197,64659874],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThiMTQ2YzQwMmQ0NTdhNjE2Y2E0ZjkyYzJkMWJlNDA5MWJjOGYwN2FhYjdiMjM4OTFiYTI4MGU1ZTI5NjMyNiJ9fX0=\"}]}}} 1")
        ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
        menu_gui_X.setItem(27,ITEMSTACK)


        Player_In_Event!!.openInventory(menu_gui_X)
    }
//######################################################################################################################
//######################################################################################################################
    fun Open_Party_Menu(){
        MC_Version_Detection()
        var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))

        Player_In_Event!!.addScoreboardTag("IS_IN_PARTY_MENU_LAST_TIME")
        //Open_GUI_From_File
        var Yml_UI = YamlConfiguration.loadConfiguration(File("$main_dir/UI/Party_Menu.yml"))
        var menu_gui_P = Bukkit.createInventory(null,Yml_UI["Surface_UI.Size"] as Int,"§2§lParty Menu                                                                                                                                                                                                                                                     <===>Party_Menu<===>___<===>Party_UUID===>>>" + Player_DATA_PARTY["Party"] + "<===>___<===>Locked_slot>>>0_1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24_25_26_27_28_29_30_31_32_33_34_35_36_37_38_39_40_41_42_43_44_45_46_47_48_49_50_51_52_53_54_55_56_57_58_59_60_61_62_63_64_65_66_67_68_69_70_71_72_73_74_75_76_77_78_79_80_81_82_83_84_85_86_87_88_89<===>___<===>Shift_locked_from_player_inv<===>")
        var content = (Yml_UI["Surface_UI.Contents"] as List<ItemStack?>).toTypedArray()
        menu_gui_P.setContents(content)

        var PARTY_DATA : YamlConfiguration
        if (Player_DATA_PARTY["Party"] == ""){
            var RANDOM_UUID = UUID.randomUUID().toString()

            setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Party",RANDOM_UUID)
            setYmlValue("$main_dir/Party/Party_DATA/$RANDOM_UUID.yml","Party_Type",0)
            setYmlValue("$main_dir/Party/Party_DATA/$RANDOM_UUID.yml","Min_Combat_Level",0)
            setYmlValue("$main_dir/Party/Party_DATA/$RANDOM_UUID.yml","Description","None")
            setYmlValue("$main_dir/Party/Party_DATA/$RANDOM_UUID.yml","Status","Semi_Public")

            setYmlValue("$main_dir/Party/Party_DATA/$RANDOM_UUID.yml","Max_Player",15)
            setYmlValue("$main_dir/Party/Party_DATA/$RANDOM_UUID.yml","Leader",Player_UUID)
            var Members_List = ArrayList<String>()
            Members_List.add(Player_UUID)
            setYmlValue("$main_dir/Party/Party_DATA/$RANDOM_UUID.yml","Members",Members_List)

            Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))
        }
        if (Player_DATA_PARTY["Party"] != ""){
            var PARTY_UUID = Player_DATA_PARTY["Party"]
            PARTY_DATA = YamlConfiguration.loadConfiguration(File("$main_dir/Party/Party_DATA/$PARTY_UUID.yml"))

            ////

            var Desc = PARTY_DATA["Description"] as String
            var Desc_List = ArrayList<String>()
            var Desc_IN_cmd = ""
            var Limit_Length = 36
            var TEMP_WIDTH_PER_TEXT = 0.0

            var PROCESS_TEMP_String = ""

            var HEAD_TEMP_String = ""
            var HEAD_TEMP_String_Width = 0.0
            var PROCESS_TEMP_String_Width = 0.0

            for(i in Desc.indices){
                if (Desc.substring(i,i+1).toByteArray().size == 1 || Desc.substring(i,i+1) == " "){
                    TEMP_WIDTH_PER_TEXT = 1.5
                }else{
                    TEMP_WIDTH_PER_TEXT = Desc.substring(i,i+1).toByteArray().size.toDouble()
                }
                PROCESS_TEMP_String_Width += TEMP_WIDTH_PER_TEXT
                PROCESS_TEMP_String += Desc.substring(i,i+1)
                if (HEAD_TEMP_String_Width + PROCESS_TEMP_String_Width <= Limit_Length){

                    if (Desc.substring(i,i+1) == " "){
                        //"AAAA 我我我我 BBBB 你你你你你A CCCCCCCCC DDDDDDDDDDDDDDDDDDDDD12345"
                        HEAD_TEMP_String = HEAD_TEMP_String + PROCESS_TEMP_String //ABC
                        HEAD_TEMP_String_Width = HEAD_TEMP_String_Width + PROCESS_TEMP_String_Width - 1.5
                        PROCESS_TEMP_String_Width = 1.5
                        PROCESS_TEMP_String = ""
                    }else{
                        if (i == Desc.lastIndex){
                            Desc_List.add(PROCESS_TEMP_String.trim())
                        }
                    }

                }else{
                    if (HEAD_TEMP_String_Width == 0.0){

                        PROCESS_TEMP_String_Width = TEMP_WIDTH_PER_TEXT
                        Desc_List.add(PROCESS_TEMP_String.dropLast(1))
                        PROCESS_TEMP_String = PROCESS_TEMP_String.takeLast(1)

                    }else{
                        Desc_List.add(HEAD_TEMP_String.trim())
                        HEAD_TEMP_String = ""
                        HEAD_TEMP_String_Width = 0.0
                    }
                }
            }
            for(s in Desc_List.indices){
                Desc_IN_cmd = Desc_IN_cmd + "'{\"text\":\"" + Desc_List[s] + "\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}',"
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "spruce_sign{display:{Name:'{\"text\":\"Description\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}',Lore:['[{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Current Value \",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false},{\"text\":\"ᐁ\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}]'," + Desc_IN_cmd + "'[{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3E92A3\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#549EAD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#69AAB8\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#7FB6C2\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Left-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to modify\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Right-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to reset\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','{\"text\":\" \"}']},BlockEntityTag:{}} 1")
            ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
            menu_gui_P.setItem(9,ITEMSTACK)

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "experience_bottle{display:{Name:'{\"text\":\"Min_Combat_Level\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}',Lore:['{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false}','{\"text\":\"Players who are at or above\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"this level can join the Party\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false}','[{\"text\":\"Current Value \",\"color\":\"dark_green\",\"bold\":false,\"italic\":false},{\"text\":\"ᐁ\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}]','{\"text\":\"" + PARTY_DATA["Min_Combat_Level"] + "\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}','{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#298699\",\"bold\":false,\"italic\":false}','{\"text\":\"Click to Modify\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\" \"}']}} 1")
            ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
            menu_gui_P.setItem(18,ITEMSTACK)

            var TIT_0_ICON = "-" ;var TIT_1_ICON = "-" ;var TIT_2_ICON = "-" ;var TIT_3_ICON = "-" ;var TIT_4_ICON = "-" ;var TIT_5_ICON = "-" ;var TIT_6_ICON = "-" ;
            var TIT_0_COLOR = "#AD976F" ; var TIT_1_COLOR = "#AD976F" ; var TIT_2_COLOR = "#AD976F" ; var TIT_3_COLOR = "#AD976F" ; var TIT_4_COLOR = "#AD976F" ; var TIT_5_COLOR = "#AD976F" ; var TIT_6_COLOR = "#AD976F" ;
            var SUB_0_COLOR = "#AD976F" ; var SUB_1_COLOR = "#AD976F" ; var SUB_2_COLOR = "#AD976F" ; var SUB_3_COLOR = "#AD976F" ; var SUB_4_COLOR = "#AD976F" ; var SUB_5_COLOR = "#AD976F" ; var SUB_6_COLOR = "#AD976F" ;

            if (PARTY_DATA["Party_Type"] == 0 ){ TIT_0_ICON = "ᐉ" ; TIT_0_COLOR = "gold" ; SUB_0_COLOR = "dark_purple"
            }
            if (PARTY_DATA["Party_Type"] == 1 ){ TIT_1_ICON = "ᐉ" ; TIT_1_COLOR = "gold" ; SUB_1_COLOR = "dark_purple"
            }
            if (PARTY_DATA["Party_Type"] == 2 ){ TIT_2_ICON = "ᐉ" ; TIT_2_COLOR = "gold" ; SUB_2_COLOR = "dark_purple"
            }
            if (PARTY_DATA["Party_Type"] == 3 ){ TIT_3_ICON = "ᐉ" ; TIT_3_COLOR = "gold" ; SUB_3_COLOR = "dark_purple"
            }
            if (PARTY_DATA["Party_Type"] == 4 ){ TIT_4_ICON = "ᐉ" ; TIT_4_COLOR = "gold" ; SUB_4_COLOR = "dark_purple"
            }
            if (PARTY_DATA["Party_Type"] == 5 ){ TIT_5_ICON = "ᐉ" ; TIT_5_COLOR = "gold" ; SUB_5_COLOR = "dark_purple"
            }
            if (PARTY_DATA["Party_Type"] == 6 ){ TIT_6_ICON = "ᐉ" ; TIT_6_COLOR = "gold" ; SUB_6_COLOR = "dark_purple"
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'{\"text\":\"Party Type\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false}',Lore:['[{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#1189BD\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#227DBC\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#3370BB\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#4464BA\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#5657BA\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#674BB9\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#783EB8\",\"bold\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#8932B7\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#984EBE\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#A86AC4\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#B786CB\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#C6A1D2\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#D5BDD9\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#E5D9DF\",\"bold\":false,\"italic\":false},{\"text\":\"▣▣\",\"font\":\"uniform\",\"color\":\"#F4F5E6\",\"bold\":false,\"italic\":false}]','{\"text\":\"Set the Party Type\",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false}','{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#1189BD\",\"bold\":false,\"italic\":false}','[{\"text\":\"" + TIT_0_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_0_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Grinding Mobs\",\"font\":\"uniform\",\"color\":\"" + SUB_0_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_1_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_1_COLOR+ "\",\"bold\":false,\"italic\":false},{\"text\":\"Fighting Boss\",\"font\":\"uniform\",\"color\":\"" + SUB_1_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_2_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_2_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Dungeon Challenge\",\"font\":\"uniform\",\"color\":\"" + SUB_2_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_3_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_3_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Quest\",\"font\":\"uniform\",\"color\":\"" + SUB_3_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_4_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_4_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Social\",\"font\":\"uniform\",\"color\":\"" + SUB_4_COLOR + "\",\"bold\":false,\"italic\":false}]','[{\"text\":\"" + TIT_5_ICON + " \",\"font\":\"uniform\",\"color\":\"" + TIT_5_COLOR + "\",\"bold\":false,\"italic\":false},{\"text\":\"Others\",\"font\":\"uniform\",\"color\":\"" + SUB_5_COLOR + "\",\"bold\":false,\"italic\":false}]','{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#3370BB\",\"bold\":false,\"italic\":false}','[{\"text\":\"Left-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to go down\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Right-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to go up\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','{\"text\":\" \"}']},SkullOwner:{Id:[I;968138172,-1748742378,-2026870197,64659874],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThiMTQ2YzQwMmQ0NTdhNjE2Y2E0ZjkyYzJkMWJlNDA5MWJjOGYwN2FhYjdiMjM4OTFiYTI4MGU1ZTI5NjMyNiJ9fX0=\"}]}}} 1")
            ITEMSTACK = Player_In_Event!!.enderChest.getItem(0)!!
            menu_gui_P.setItem(27,ITEMSTACK)
            ////

            var Members_List = ArrayList<String>()
            if (!(PARTY_DATA["Members"] as ArrayList<String>).isNullOrEmpty() ){
                Members_List = PARTY_DATA["Members"] as ArrayList<String>
                for (i in 0..14){

                    if (i <= Members_List.lastIndex){
                        if (File("$main_dir/Player_DATA/" + Members_List[i] + "/Common.yml").exists()){
                            var Player_DATA_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/" + Members_List[i] + "/Common.yml"))

                            if (Members_List[i] == PARTY_DATA["Leader"]){
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"♚ \",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_COM["UserName"] + "\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#61D0E0\",\"bold\":false,\"italic\":false}','[{\"text\":\"Left-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to Open Interaction Gui\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Right-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to Manage\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#66AD9F\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;853408908,-902150958,-1544141899,-531255144],Properties:{textures:[{Value:\"" + Player_DATA_COM["Head_Texture_Value"] + "\"}]}}} 1")
                            }else{
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_COM["UserName"] + "\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#61D0E0\",\"bold\":false,\"italic\":false}','[{\"text\":\"Left-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to Open Interaction Gui\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','[{\"text\":\"Right-Click \",\"font\":\"uniform\",\"color\":\"gray\",\"bold\":false,\"italic\":false},{\"text\":\"to Manage\",\"font\":\"uniform\",\"color\":\"#AD976F\",\"bold\":false,\"italic\":false}]','{\"text\":\"▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣\",\"font\":\"uniform\",\"color\":\"#66AD9F\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;853408908,-902150958,-1544141899,-531255144],Properties:{textures:[{Value:\"" + Player_DATA_COM["Head_Texture_Value"] + "\"}]}}} 1")

                            }
                            menu_gui_P.setItem(11 + i + ((i/5)*4) ,Player_In_Event!!.enderChest.getItem(0))
                        }
                    }
                    if (i >= (PARTY_DATA["Max_Player"] as Int)){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "red_stained_glass_pane{display:{Name:'{\"text\":\" \"}'}} 1")
                        menu_gui_P.setItem(11 + i + ((i/5)*4) ,Player_In_Event!!.enderChest.getItem(0))
                    }

                }
            }
        }
        Player_In_Event!!.openInventory(menu_gui_P)
    }
//######################################################################################################################
//######################################################################################################################
//When_Open_ANY_GUI
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryOpen_VBSX(e: InventoryOpenEvent){

        e.player.removeScoreboardTag("IS_GUI_CLOSED")
    }
//######################################################################################################################
//######################################################################################################################
//When_Close_ANY_GUI
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryClose_VBS(e: InventoryCloseEvent){

        if (!e.player.scoreboardTags.contains("CLOSE_1_TIME")){

            e.player.scoreboardTags.add("IS_GUI_CLOSED")
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, {
                (e.player as Player).updateInventory()
                if (e.player.scoreboardTags.contains("IS_GUI_CLOSED")){
                    e.player.addScoreboardTag("CLOSE_1_TIME")
                    e.player.closeInventory()
                }
            },1L)
        }else{
            e.player.removeScoreboardTag("CLOSE_1_TIME")
        }
    }
//######################################################################################################################
//######################################################################################################################
//SHIFT_RIGHT_CLICK_PLAYER >>> OPEN_INTERACTION_GUI
    @EventHandler
    fun SHIFT_RIGHT_CLICK_PLAYER(e:PlayerInteractEntityEvent){
        if (e.rightClicked.type.equals(EntityType.PLAYER)){
            if (e.player.isSneaking){
                var TARGET_PLAYER = e.rightClicked as Player
                Bukkit.dispatchCommand(e.player,"interactiongui " + TARGET_PLAYER.uniqueId.toString())
            }
        }
    }
//######################################################################################################################
//######################################################################################################################
    @EventHandler
    fun onInventoryClick_ASDF(e: InventoryClickEvent){
    Player_In_Event = e.whoClicked as Player
    Player_UUID = Player_In_Event!!.uniqueId.toString()
    //==================================================================================================================
    //INTERACTION_GUI_RUNNING VVV
    //==================================================================================================================
        if (e.view.title.contains("<===>Interaction_Gui<===>")){
            var TAR_UUID = e.view.title.split("<===>Player_UUID>>>")[1].split("<===>")[0]

            if (e.rawSlot == 10){
                var Player_DATA_FRI_L = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Friends_List.yml"))
                var ARR = ArrayList<String>()
                ARR = Player_DATA_FRI_L["Friends"] as ArrayList<String>
                if (!ARR.contains("" + TAR_UUID)){
                    Bukkit.dispatchCommand(Player_In_Event!!,"friend add " + TAR_UUID)
                }
                if (ARR.contains("" + TAR_UUID)){
                    Bukkit.dispatchCommand(Player_In_Event!!,"friend remove " + TAR_UUID)
                }

            }
            if (e.rawSlot == 37){
                var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml"))
                var Contacts = ArrayList<String>()
                if (Player_DATA_CH["PrivateChat.Contacts"] != null)Contacts = Player_DATA_CH["PrivateChat.Contacts"] as ArrayList<String>
                if (Contacts.contains(TAR_UUID)){
                    Contacts.remove(TAR_UUID)
                    Contacts.add(0,TAR_UUID)
                    setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","PrivateChat.Contacts",Contacts)
                }
                Bukkit.dispatchCommand(Player_In_Event!!,"channel 2 force_a78315")
                Bukkit.dispatchCommand(Player_In_Event!!,"privatechatselect " + TAR_UUID)
                Player_In_Event!!.closeInventory()
            }
        }
    //==================================================================================================================
    //Confirmation_GUI VVV
    //==================================================================================================================
        if (e.view.title.contains("<===>Confirmation_GUI_OF_REMOVE_FRIEND<===>")){
            var Player_DATA_FRI_L = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Friends_List.yml"))
            if (e.rawSlot == 11){

                var THE_B_SIDE_UUID = e.view.title.split("<===>Contact_UUID>>>")[1].split("<===>")[0]
                if (File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Common.yml").exists() ){

                    var FRIEND_LIST = ArrayList<String>()
                    FRIEND_LIST = Player_DATA_FRI_L["Friends"] as ArrayList<String>

                    if (FRIEND_LIST.contains(THE_B_SIDE_UUID)){
                        var Player_DATA_FRI_L_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Friends_List.yml"))
                        var Player_DATA_COM_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Common.yml"))

                        //==================================================================================
                        FRIEND_LIST.remove(THE_B_SIDE_UUID)
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Friends_List.yml","Friends",FRIEND_LIST)

                        FRIEND_LIST = Player_DATA_FRI_L_2["Friends"] as ArrayList<String>
                        FRIEND_LIST.remove(Player_UUID)
                        setYmlValue("$main_dir/Player_DATA/$THE_B_SIDE_UUID/Friends_List.yml","Friends",FRIEND_LIST)
                        //==================================================================================
                        if (Player_In_Event!!.openInventory.title.contains("<===>Interaction_Gui<===>")){
                            if (Player_In_Event!!.openInventory.title.split("<===>Player_UUID>>>")[1].split("<===>")[0] == THE_B_SIDE_UUID){

                                MC_Version_Detection()
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"[\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"+\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"] Add Friend\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]'},SkullOwner:{Id:[I;853408908,-902150958,-1544141899,-531255144],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19\"}]}}} 1")

                                Player_In_Event!!.openInventory.setItem(10,Player_In_Event!!.enderChest.getItem(0))

                            }
                        }

                        if (Player_DATA_COM_2["IsOnline"] == "True"){
                            if (Bukkit.getPlayer(UUID.fromString(THE_B_SIDE_UUID))!!.openInventory.title.contains("<===>Interaction_Gui<===>")){

                                if (Bukkit.getPlayer(UUID.fromString(THE_B_SIDE_UUID))!!.openInventory.title.split("<===>Player_UUID>>>")[1].split("<===>")[0] == Player_In_Event!!.uniqueId.toString()){

                                    Player_In_Event = Bukkit.getPlayer(UUID.fromString(THE_B_SIDE_UUID))!!
                                    MC_Version_Detection()
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"[\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"+\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"] Add Friend\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]'},SkullOwner:{Id:[I;853408908,-902150958,-1544141899,-531255144],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19\"}]}}} 1")

                                    Player_In_Event!!.openInventory.setItem(10,Player_In_Event!!.enderChest.getItem(0))
                                    Player_In_Event = e.whoClicked as Player
                                }
                            }
                        }
                    }

                }
                Bukkit.dispatchCommand(Player_In_Event!!,"interactiongui $THE_B_SIDE_UUID")

            }
            if (e.rawSlot == 15){
                var THE_B_SIDE_UUID = e.view.title.split("<===>Contact_UUID>>>")[1].split("<===>")[0]
                Bukkit.dispatchCommand(Player_In_Event!!,"interactiongui $THE_B_SIDE_UUID")
            }
        }
    //==================================================================================================================
    //When_Click_On_Notice_GUI VVV
    //==================================================================================================================
        if (e.view.title.contains("<===>Notice_GUI<===>")){
            GUI_Page = e.view.title.split("<===>GUI_PAGE>>>")[1].split("<===>")[0].toInt()

            if (e.rawSlot == 44){
                GUI_Page -= 1
                Bukkit.dispatchCommand(Player_In_Event!!,"channel 5")
            }
            if (e.rawSlot == 53){
                GUI_Page += 1
                Bukkit.dispatchCommand(Player_In_Event!!,"channel 5")
            }

            var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml"))
            if (e.currentItem != null){

                //accept friend request VVV
                if (e.currentItem!!.itemMeta!!.localizedName.contains("_whishes_to_be_your_friend")){

                    var B_SIDE_UUID = e.currentItem!!.itemMeta!!.localizedName.split("_whishes_to_be_your_friend")[0]
                    var NOTICE_LIST = ArrayList<String>()

                    if (e.isLeftClick){

                        Bukkit.dispatchCommand(Player_In_Event!!,"friend add " + B_SIDE_UUID)
                        Player_In_Event = e.whoClicked as Player
                    }
                    if (e.isRightClick){

                        if (!(Player_DATA_CH["Channel.Notice"] as ArrayList<String>).isNullOrEmpty()){

                            NOTICE_LIST = Player_DATA_CH["Channel.Notice"] as ArrayList<String>
                            NOTICE_LIST.remove(B_SIDE_UUID + "_whishes_to_be_your_friend")
                            NOTICE_LIST.remove(B_SIDE_UUID + "_whishes_to_be_your_friend_UNREAD")
                            setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Notice",NOTICE_LIST)
                        }
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this, {
                        Bukkit.dispatchCommand(Player_In_Event!!,"channel 5 force_a78315")
                    }, 1L)

                }
                //accept party invitation VVV
                if (e.currentItem!!.itemMeta!!.localizedName.contains("_whishes_to_invite_you_to_join_the_party")){

                    var B_SIDE_UUID = e.currentItem!!.itemMeta!!.localizedName.split("_whishes_to_invite_you_to_join_the_party")[0]
                    var NOTICE_LIST = ArrayList<String>()
                    var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))
                    var B_SIDE_PARTY_DATA : YamlConfiguration

                    if (e.isLeftClick){
                        if (Player_DATA_PARTY["Party"] == ""){
                            if (File("$main_dir/Player_DATA/$B_SIDE_UUID/Party.yml").exists()){
                                var B_SIDE_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$B_SIDE_UUID/Party.yml"))
                                if (B_SIDE_DATA_PARTY["Party"] != ""){
                                    var Party_UUID = B_SIDE_DATA_PARTY["Party"]
                                    B_SIDE_PARTY_DATA = YamlConfiguration.loadConfiguration(File("$main_dir/Party/Party_DATA/$Party_UUID.yml"))

                                    if (B_SIDE_PARTY_DATA["Leader"] == B_SIDE_UUID){
                                        var MEMBER_LIST = ArrayList<String>()
                                        var MEMBER_COUNT = 0
                                        if (!(B_SIDE_PARTY_DATA["Members"] as ArrayList<String>).isNullOrEmpty() ){
                                            MEMBER_COUNT = (B_SIDE_PARTY_DATA["Members"] as ArrayList<String>).size
                                            MEMBER_LIST = (B_SIDE_PARTY_DATA["Members"] as ArrayList<String>)
                                        }
                                        if (MEMBER_COUNT < (B_SIDE_PARTY_DATA["Max_Player"] as Int) ){
                                            MEMBER_LIST.add(Player_UUID)
                                            setYmlValue("$main_dir/Party/Party_DATA/$Party_UUID.yml","Members",MEMBER_LIST)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (e.isRightClick){
                    }
                    if (!(Player_DATA_CH["Channel.Notice"] as ArrayList<String>).isNullOrEmpty()){

                        NOTICE_LIST = Player_DATA_CH["Channel.Notice"] as ArrayList<String>
                        NOTICE_LIST.remove(B_SIDE_UUID + "_whishes_to_invite_you_to_join_the_party")
                        NOTICE_LIST.remove(B_SIDE_UUID + "_whishes_to_invite_you_to_join_the_party_UNREAD")
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Notice",NOTICE_LIST)
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(this, {
                        Bukkit.dispatchCommand(Player_In_Event!!,"channel 5 force_a78315")
                    }, 1L)

                }
                //accept party joining request VVV
                if (e.currentItem!!.itemMeta!!.localizedName.contains("_whishes_to_join_your_party")){

                    var B_SIDE_UUID = e.currentItem!!.itemMeta!!.localizedName.split("_whishes_to_join_your_party")[0]
                    var NOTICE_LIST = ArrayList<String>()
                    var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))
                    var A_SIDE_PARTY_DATA : YamlConfiguration

                    if (e.isLeftClick){
                        if (Player_DATA_PARTY["Party"] != ""){
                            var Party_UUID = Player_DATA_PARTY["Party"]
                            A_SIDE_PARTY_DATA = YamlConfiguration.loadConfiguration(File("$main_dir/Party/Party_DATA/$Party_UUID.yml"))
                            if (A_SIDE_PARTY_DATA["Leader"] == Player_UUID){
                                var B_SIDE_DATA_PARTY : YamlConfiguration

                                if (File("$main_dir/Player_DATA/$B_SIDE_UUID/Party.yml").exists()){
                                    B_SIDE_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$B_SIDE_UUID/Party.yml"))
                                    if (B_SIDE_DATA_PARTY["Party"] == ""){

                                        var MEMBER_LIST = ArrayList<String>()
                                        var MEMBER_COUNT = 0
                                        if (!(A_SIDE_PARTY_DATA["Members"] as ArrayList<String>).isNullOrEmpty() ){
                                            MEMBER_COUNT = (A_SIDE_PARTY_DATA["Members"] as ArrayList<String>).size
                                            MEMBER_LIST = (A_SIDE_PARTY_DATA["Members"] as ArrayList<String>)
                                        }
                                        if (MEMBER_COUNT < (A_SIDE_PARTY_DATA["Max_Player"] as Int) ){
                                            MEMBER_LIST.add(B_SIDE_UUID)
                                            setYmlValue("$main_dir/Party/Party_DATA/$Party_UUID.yml","Members",MEMBER_LIST)
                                        }
                                    }
                                }
                            }
                        }
                        Bukkit.dispatchCommand(Player_In_Event!!,"friend add " + B_SIDE_UUID)
                        Player_In_Event = e.whoClicked as Player
                    }
                    if (e.isRightClick){

                    }
                    if (!(Player_DATA_CH["Channel.Notice"] as ArrayList<String>).isNullOrEmpty()){

                        NOTICE_LIST = Player_DATA_CH["Channel.Notice"] as ArrayList<String>
                        NOTICE_LIST.remove(B_SIDE_UUID + "_whishes_to_join_your_party")
                        NOTICE_LIST.remove(B_SIDE_UUID + "_whishes_to_join_your_party_UNREAD")
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Notice",NOTICE_LIST)
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(this, {
                        Bukkit.dispatchCommand(Player_In_Event!!,"channel 5 force_a78315")
                    }, 1L)

                }
            }
        }
    //==================================================================================================================
    //When_Click_On_Friend_GUI VVV
    //==================================================================================================================
        if (e.view.title.contains("<===>Friend_List<===>")){
            if (e.currentItem != null){

                ITEMSTACK = e.currentItem!!
                ITEMSTACK_mdf = ITEMSTACK.itemMeta
                if (ITEMSTACK_mdf!!.localizedName.contains("UUID===>>>")){
                    var selected_player_UUID = ITEMSTACK_mdf!!.localizedName.split("UUID===>>>")[1]
                    Bukkit.dispatchCommand(Player_In_Event!!,"interactiongui " + selected_player_UUID)
                }
                if (e.rawSlot == 44){
                    GUI_Page = e.view.title.split("<===>GUI_PAGE>>>")[1].split("<===>")[0].toInt()
                    GUI_Page -= 1
                    Bukkit.dispatchCommand(Player_In_Event!!,"friend list " + GUI_Page)
                }
                if (e.rawSlot == 53){
                    GUI_Page = e.view.title.split("<===>GUI_PAGE>>>")[1].split("<===>")[0].toInt()
                    GUI_Page += 1
                    Bukkit.dispatchCommand(Player_In_Event!!,"friend list " + GUI_Page)
                }

            }
        }
    //==================================================================================================================
    //When_Click_On_Party_Finder_GUI VVV
    //==================================================================================================================
        if (e.view.title.contains("<===>Party_Finder<===>")){

            if (e.rawSlot == 8){
                Open_Party_Menu()
            }
            if (e.rawSlot == 9){
                if (e.isLeftClick){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"system_message add $Player_UUID Enter_Something_As_Value dummy §6§lPlease type something...")
                    ADD_PLAYER_TAGS(Player_UUID,"Disable_Message_Dispatch")
                    ADD_PLAYER_TAGS(Player_UUID,"Party_String_Filter")
                    CHAT_BOX_UPDATE(Player_UUID)
                    Player_In_Event!!.closeInventory()
                }
                if (e.isRightClick){
                    setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","String_Filter","")

                    Bukkit.dispatchCommand(Player_In_Event!!,"party")
                }
            }
            if (e.rawSlot == 18){
                var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))

                if (e.isLeftClick){
                    if ((Player_DATA_PARTY["Level_Filter"] as Int) < 300 ){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Level_Filter",(Player_DATA_PARTY["Level_Filter"] as Int) + 1)
                    }
                    if (e.isShiftClick){
                        if ((Player_DATA_PARTY["Level_Filter"] as Int) + 10 <= 300 ){
                            setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Level_Filter",(Player_DATA_PARTY["Level_Filter"] as Int) + 10)
                        }else{
                            setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Level_Filter",300)
                        }
                    }
                }
                if (e.isRightClick){
                    if ((Player_DATA_PARTY["Level_Filter"] as Int) > 0 ){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Level_Filter",(Player_DATA_PARTY["Level_Filter"] as Int) - 1)
                    }
                    if (e.isShiftClick){
                        if ((Player_DATA_PARTY["Level_Filter"] as Int) - 10 >= 0 ){
                            setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Level_Filter",(Player_DATA_PARTY["Level_Filter"] as Int) - 10)
                        }else{
                            setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Level_Filter",0)
                        }
                    }
                }
                Bukkit.dispatchCommand(Player_In_Event!!,"party")
            }
            if (e.rawSlot == 27){
                var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))

                if (e.isLeftClick){
                    if ((Player_DATA_PARTY["Party_Type_Filter"] as Int) < 6){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Party_Type_Filter",(Player_DATA_PARTY["Party_Type_Filter"] as Int) + 1)
                    }else{
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Party_Type_Filter",0)
                    }
                }
                if (e.isRightClick){
                    if ((Player_DATA_PARTY["Party_Type_Filter"] as Int) > 0){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Party_Type_Filter",(Player_DATA_PARTY["Party_Type_Filter"] as Int) - 1)
                    }else{
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Party_Type_Filter",6)
                    }
                }
                Bukkit.dispatchCommand(Player_In_Event!!,"party")

            }
            if (e.rawSlot == 36){
                var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))

                setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","Show_Full_Parties",!(Player_DATA_PARTY["Show_Full_Parties"] as Boolean))
                Bukkit.dispatchCommand(Player_In_Event!!,"party")
            }
            if (e.rawSlot == 44){
                GUI_Page = e.view.title.split("<===>GUI_PAGE>>>")[1].split("<===>")[0].toInt()
                GUI_Page -= 1
                Bukkit.dispatchCommand(Player_In_Event!!,"party")
            }
            if (e.rawSlot == 53){
                GUI_Page = e.view.title.split("<===>GUI_PAGE>>>")[1].split("<===>")[0].toInt()
                GUI_Page += 1
                Bukkit.dispatchCommand(Player_In_Event!!,"party")
            }

        }
    //==================================================================================================================
    //When_Click_On_Party_Menu_GUI VVV
    //==================================================================================================================
        if (e.view.title.contains("<===>Party_Menu<===>")){
            var Party_UUID = e.view.title.split("<===>Party_UUID===>>>")[1].split("<===>")[0]
            if (e.rawSlot == 9){
                if (e.isLeftClick){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"system_message add $Player_UUID Enter_Something_As_Value dummy §6§lPlease type something...")
                    ADD_PLAYER_TAGS(Player_UUID,"Disable_Message_Dispatch")
                    ADD_PLAYER_TAGS(Player_UUID,"Party_Description")
                    CHAT_BOX_UPDATE(Player_UUID)
                    Player_In_Event!!.closeInventory()
                }
                if (e.isRightClick){
                    setYmlValue("$main_dir/Party/Party_DATA/$Party_UUID.yml","Description","")

                    Bukkit.dispatchCommand(Player_In_Event!!,"party")
                }
            }
            if (e.rawSlot == 17){
                Open_Party_Finder()
            }
            if (e.rawSlot == 18){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"system_message add $Player_UUID Enter_Something_As_Value dummy §6§lPlease type a number")
                ADD_PLAYER_TAGS(Player_UUID,"Disable_Message_Dispatch")
                ADD_PLAYER_TAGS(Player_UUID,"Party_Min_Combat_Level")
                CHAT_BOX_UPDATE(Player_UUID)
                Player_In_Event!!.closeInventory()
            }
            if (e.rawSlot == 27){
                var PARTY_DATA = YamlConfiguration.loadConfiguration(File("$main_dir/Party/Party_DATA/$Party_UUID.yml"))
                if (e.isLeftClick){
                    if ((PARTY_DATA["Party_Type"] as Int) < 5 ){
                        setYmlValue("$main_dir/Party/Party_DATA/$Party_UUID.yml","Party_Type",(PARTY_DATA["Party_Type"] as Int) + 1)
                    }else{
                        setYmlValue("$main_dir/Party/Party_DATA/$Party_UUID.yml","Party_Type",0)
                    }
                }
                if (e.isRightClick){
                    if ((PARTY_DATA["Party_Type"] as Int) > 0 ){
                        setYmlValue("$main_dir/Party/Party_DATA/$Party_UUID.yml","Party_Type",(PARTY_DATA["Party_Type"] as Int) - 1)
                    }else{
                        setYmlValue("$main_dir/Party/Party_DATA/$Party_UUID.yml","Party_Type",5)
                    }
                }
                Open_Party_Menu()
            }

        }
    //==================================================================================================================
    //Channel_Setting VVV
    //==================================================================================================================
        if (e.view.title.contains("<===>Channel_Setting<===>")){
            var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml"))

            MC_Version_Detection()
            var THE_CHANNEL_NAME = ""
            var OKK = "NO"
            if (e.rawSlot == 10){
                if (e.isLeftClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_0_Notify"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_0_Notify","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_0_Notify"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_0_Notify","Enable")
                }
                if (e.isRightClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_0_Play_Sound"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_0_Play_Sound","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_0_Play_Sound"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_0_Play_Sound","Enable")
                }

                THE_CHANNEL_NAME = "All"
                Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + THE_CHANNEL_NAME + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_0_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_0_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;2018521520,1627735941,-1882266884,758719831],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0N2EzOTQ5OWRlNDllMjRjODkyYjA5MjU2OTQzMjkyN2RlY2JiNzM5OWUxMTg0N2YzMTA0ZmRiMTY1YjZkYyJ9fX0=\"}]}}} 1")
                Player_In_Event!!.openInventory.topInventory.setItem(10,Player_In_Event!!.enderChest.getItem(0))
            }
            if (e.rawSlot == 13){
                if (e.isLeftClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_1_Notify"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_1_Notify","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_1_Notify"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_1_Notify","Enable")
                }
                if (e.isRightClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_1_Play_Sound"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_1_Play_Sound","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_1_Play_Sound"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_1_Play_Sound","Enable")
                }

                THE_CHANNEL_NAME = "Region"
                Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml.yml"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Region\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_1_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_1_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;1590212200,-317177619,-1968522945,379510964],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhmZDcxMjZjZDY3MGM3OTcxYTI4NTczNGVkZmRkODAyNTcyYTcyYTNmMDVlYTQxY2NkYTQ5NDNiYTM3MzQ3MSJ9fX0=\"}]}}} 1")
                Player_In_Event!!.openInventory.topInventory.setItem(13,Player_In_Event!!.enderChest.getItem(0))
            }
            if (e.rawSlot == 16){
                if (e.isLeftClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_2_Notify"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_2_Notify","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_2_Notify"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_2_Notify","Enable")
                }
                if (e.isRightClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_2_Play_Sound"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_2_Play_Sound","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_2_Play_Sound"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_2_Play_Sound","Enable")
                }

                THE_CHANNEL_NAME = "Private"
                Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml.yml"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Private\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_2_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_2_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;-1199100648,-1480964910,-1232196606,889593674],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ0MGIwYWNiZjk4YzY3YzU1ZjIwYmMxOWQ5NDkxZGU0OWYzYzhhMjBkZTkwNWUzZmFkNWIzZGU4YjRkYjdiOCJ9fX0=\"}]}}} 1")
                Player_In_Event!!.openInventory.topInventory.setItem(16,Player_In_Event!!.enderChest.getItem(0))
            }
            if (e.rawSlot == 37){
                if (e.isLeftClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_3_Notify"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_3_Notify","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_3_Notify"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_3_Notify","Enable")
                }
                if (e.isRightClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_3_Play_Sound"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_3_Play_Sound","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_3_Play_Sound"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_3_Play_Sound","Enable")
                }

                THE_CHANNEL_NAME = "Party"
                Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml.yml"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Party\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_3_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_3_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;-1672030017,-1658041606,-1468098958,-1280689632],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE1MzYxYjUyZGFmNGYxYzVjNTQ4MGEzOWZhYWExMDg5NzU5NWZhNTc2M2Y3NTdiZGRhMzk1NjU4OGZlYzY3OCJ9fX0=\"}]}}} 1")
                Player_In_Event!!.openInventory.topInventory.setItem(37,Player_In_Event!!.enderChest.getItem(0))
            }
            if (e.rawSlot == 40){
                if (e.isLeftClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_4_Notify"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","$Player_UUID.Channel.Setting.Channel_4_Notify","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_4_Notify"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_4_Notify","Enable")
                }
                if (e.isRightClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_4_Play_Sound"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","$Player_UUID.Channel.Setting.Channel_4_Play_Sound","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_4_Play_Sound"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_4_Play_Sound","Enable")
                }

                THE_CHANNEL_NAME = "Guild"
                Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml.yml"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Guild\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_4_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_4_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;-1867535749,442057165,-1657564529,-401811555],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzYxODQ2MTBjNTBjMmVmYjcyODViYzJkMjBmMzk0MzY0ZTgzNjdiYjMxNDg0MWMyMzhhNmE1MjFhMWVlMTJiZiJ9fX0=\"}]}}} 1")
                Player_In_Event!!.openInventory.topInventory.setItem(40,Player_In_Event!!.enderChest.getItem(0))
            }
            if (e.rawSlot == 43){
                if (e.isLeftClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_5_Notify"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_5_Notify","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_5_Notify"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_5_Notify","Enable")
                }
                if (e.isRightClick){
                    if (Player_DATA_CH["Channel.Setting.Channel_5_Play_Sound"] == "Enable" && OKK == "NO"){
                        setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml","Channel.Setting.Channel_5_Play_Sound","Disable")
                        OKK = "YES"
                    }
                    if (Player_DATA_CH["Channel.Setting.Channel_5_Play_Sound"] == "Disable" && OKK == "NO") setYmlValue("$main_dir/Player_DATA/$Player_UUID/Channel_And_Messageyml","Channel.Setting.Channel_5_Play_Sound","Enable")
                }

                THE_CHANNEL_NAME = "Notice"
                Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml.yml"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_for_set_item_in_gui + "player_head{display:{Name:'[{\"text\":\"Channel \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Notice\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]',Lore:['{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Notify me when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Left-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_5_Notify"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}','{\"text\":\"Play Sound when getting new message \",\"font\":\"uniform\",\"color\":\"#BFBFBF\",\"bold\":true,\"italic\":false}','[{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"Right-Click\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯ \",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"to Switch\",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false}]','[{\"text\":\"Current \",\"font\":\"uniform\",\"color\":\"green\",\"bold\":true,\"italic\":false},{\"text\":\"❮\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false},{\"text\":\"" + Player_DATA_CH["Channel.Setting.Channel_5_Play_Sound"] + "\",\"font\":\"uniform\",\"color\":\"gold\",\"bold\":true,\"italic\":false},{\"text\":\"❯\",\"font\":\"uniform\",\"color\":\"dark_purple\",\"bold\":false,\"italic\":false}]','{\"text\":\"=========================================\",\"font\":\"uniform\",\"color\":\"dark_green\",\"bold\":false,\"italic\":false}']},SkullOwner:{Id:[I;-1059101233,27282105,-1508354492,1680594266],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFmNWRhZGQ1MGQzODk2OWQ2NWIxMDUyZjBiOTkzNTg2ZDAyNjk2NWQ5ZDVjYzJhMmYzYzc1NTViYjJlZTY0YSJ9fX0=\"}]}}} 1")
                Player_In_Event!!.openInventory.topInventory.setItem(43,Player_In_Event!!.enderChest.getItem(0))
            }
        }
    }
//######################################################################################################################
//######################################################################################################################
//When_Open_Notice
//######################################################################################################################
//######################################################################################################################
//When_Close_Notice
//######################################################################################################################
//######################################################################################################################
    @EventHandler
    fun onInventoryClose_AVVB(e: InventoryCloseEvent){
    Player_In_Event = e.player as Player
    //==================================================================================================================
    //When_Close_Friend_GUI
    //==================================================================================================================
        if (e.view.title.contains("<===>Friend_List<===>")){
            GUI_Page = e.view.title.split("<===>GUI_PAGE>>>")[1].split("<===>")[0].toInt()

            if (Player_In_Event!!.scoreboardTags.size > 0){
                for (i in 0..Player_In_Event!!.scoreboardTags.size - 1){
                    if (Player_In_Event!!.scoreboardTags.toList()[i].contains("Friend_Page_at_Last_Time===>>>")){
                        Player_In_Event!!.removeScoreboardTag(Player_In_Event!!.scoreboardTags.toList()[i])
                    }
                }
            }
            Player_In_Event!!.addScoreboardTag("Friend_Page_at_Last_Time===>>>" + GUI_Page)
        }
    //==================================================================================================================
    //When_Close_Party_Finder_GUI
    //==================================================================================================================
        if (e.view.title.contains("<===>Party_Finder<===>")){
            THE_Player_Who_IS_Viewing_Party_Finder.remove(Player_In_Event!!.uniqueId.toString())
            GUI_Page = e.view.title.split("<===>GUI_PAGE>>>")[1].split("<===>")[0].toInt()

            if (Player_In_Event!!.scoreboardTags.size > 0){
                ZXC@for (i in 0..Player_In_Event!!.scoreboardTags.size - 1){
                    if (Player_In_Event!!.scoreboardTags.toList()[i].contains("Party_Finder_Page_at_Last_Time===>>>")){
                        Player_In_Event!!.removeScoreboardTag(Player_In_Event!!.scoreboardTags.toList()[i])
                        break@ZXC

                    }
                }
            }
            Player_In_Event!!.addScoreboardTag("Party_Finder_Page_at_Last_Time===>>>" + GUI_Page)

        }
    //==================================================================================================================
    //When_Close_Party_Menu_GUI
    //==================================================================================================================
        if (e.view.title.contains("<===>Party_Menu<===>")){

        }
    }
//######################################################################################################################
//######################################################################################################################
//CHAT_RECORD
    @EventHandler
    fun CHAT_RECORD(e: PlayerChatEvent){
        Player_In_Event = e.player
        Player_UUID = e.player.uniqueId.toString()
        e.isCancelled = true
        var LIST_OF_CHAT_LINE = ArrayList<String>()

        var Player_DATA_COM = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Common.yml"))
        var Player_DATA_CH = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Channel_And_Message.yml"))


        var RECIPIENT_TAGS = ArrayList<String>()
        if (!(Player_DATA_COM["Tags"] as ArrayList<String>).isNullOrEmpty()) RECIPIENT_TAGS = Player_DATA_COM["Tags"] as ArrayList<String>

        if (RECIPIENT_TAGS.contains("Party_String_Filter")){
            Bukkit.dispatchCommand(Player_In_Event!!,"system_message remove $Player_UUID Enter_Something_As_Value")
            var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))
            setYmlValue("$main_dir/Player_DATA/$Player_UUID/Party.yml","String_Filter",e.message)
            REMOVE_PLAYER_TAGS(Player_UUID,"Party_String_Filter")
            REMOVE_PLAYER_TAGS(Player_UUID,"Disable_Message_Dispatch")
            Bukkit.dispatchCommand(Player_In_Event!!,"party")
        }
        if (RECIPIENT_TAGS.contains("Party_Description")){
            Bukkit.dispatchCommand(Player_In_Event!!,"system_message remove $Player_UUID Enter_Something_As_Value")
            var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))
            setYmlValue("$main_dir/Party/Party_DATA/" + Player_DATA_PARTY["Party"] + ".yml","Description",e.message)
            REMOVE_PLAYER_TAGS(Player_UUID,"Party_Description")
            REMOVE_PLAYER_TAGS(Player_UUID,"Disable_Message_Dispatch")
            Bukkit.dispatchCommand(Player_In_Event!!,"party")
        }
        if (RECIPIENT_TAGS.contains("Party_Min_Combat_Level")){
            if (isInt(e.message)){
                var Player_DATA_PARTY = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$Player_UUID/Party.yml"))
                setYmlValue("$main_dir/Party/Party_DATA/" + Player_DATA_PARTY["Party"] + ".yml","Min_Combat_Level",e.message.toInt())
                REMOVE_PLAYER_TAGS(Player_UUID,"Party_Min_Combat_Level")
                REMOVE_PLAYER_TAGS(Player_UUID,"Disable_Message_Dispatch")
            }
            Bukkit.dispatchCommand(Player_In_Event!!,"system_message remove $Player_UUID Enter_Something_As_Value")
            Bukkit.dispatchCommand(Player_In_Event!!,"party")
        }

        if (!RECIPIENT_TAGS.contains("Disable_Channel") && !RECIPIENT_TAGS.contains("Disable_Message_Dispatch")){

            var CHAT_MESSAGE = e.message
            var CHAT_MESSAGE_PROCESS = e.message
            var FINAL_STRING = ""
            var CHAT_MESSAGE_PROCESSED_CURRENT = ""
            var TEXT_WIDTH = 0.0

            while (CHAT_MESSAGE_PROCESS.isNotEmpty()){

                TEXT_WIDTH = 0.0
                CHPRO@for(i in 1..CHAT_MESSAGE_PROCESS.length){

                    if (MinecraftFont.Font.isValid("" + CHAT_MESSAGE_PROCESS.substring(i-1,i))){

                        var AXA = MinecraftFont.Font.getWidth("" + CHAT_MESSAGE_PROCESS.substring(i-1,i)).toDouble()
                        if (CHAT_MESSAGE_PROCESS.substring(i-1,i) == ".") AXA = 2.5
                        if (CHAT_MESSAGE_PROCESS.substring(i-1,i) == "i") AXA = 4.5
                        if (CHAT_MESSAGE_PROCESS.substring(i-1,i) == "I") AXA = 4.5
                        if (CHAT_MESSAGE_PROCESS.substring(i-1,i) == "*") AXA = 5.125

                        TEXT_WIDTH = TEXT_WIDTH + AXA
                    }
                    if (! MinecraftFont.Font.isValid("" + CHAT_MESSAGE_PROCESS.substring(i-1,i))){

                        val font = Font("Ubuntu Mono",Font.PLAIN, 19)
                        val frc = FontRenderContext(AffineTransform(), true, true)

                        var AXA = font.getStringBounds(CHAT_MESSAGE_PROCESS.substring(i-1,i), frc).width
                        if (CHAT_MESSAGE_PROCESS.substring(i-1,i) == "`") AXA = 2.5

                        TEXT_WIDTH = TEXT_WIDTH + AXA
                    }
                    var LIMIT_TEXT_WIDTH = 270
                    if (Player_DATA_CH["Channel.CurrentChannel"] == "2") LIMIT_TEXT_WIDTH = 210
                    if (TEXT_WIDTH > LIMIT_TEXT_WIDTH || i == CHAT_MESSAGE_PROCESS.length){

                        CHAT_MESSAGE_PROCESSED_CURRENT = CHAT_MESSAGE_PROCESS.substring(0,i)

                        var HEAD_SPACE = ""
                        if (Player_DATA_CH["Channel.CurrentChannel"] == "2") HEAD_SPACE ="\\u00a7r "

                        FINAL_STRING = FINAL_STRING + "\"},\"\\n" + HEAD_SPACE + "\",{\"font\":\"uniform\",\"text\":\"" + CHAT_MESSAGE_PROCESSED_CURRENT.replace("\\","\\\\").replace("\"","\\\"")

                        CHAT_MESSAGE_PROCESS = CHAT_MESSAGE_PROCESS.substring(i,CHAT_MESSAGE_PROCESS.length)

                        break@CHPRO
                    }
                }
            }
            if (Player_DATA_CH["Channel.CurrentChannel"] == "0"){
                Yml_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Chat/Global_Chat_REC.yml"))


                if (Yml_2["Contents"] != null){
                    if ((Yml_2["Contents"] as String).split("\\n").size >= 240){
                        setYmlValue("$main_dir/Chat/Global_Chat_REC.yml","Contents",(Yml_2["Contents"] as String).substring(0,280) + (Yml_2["Contents"] as String).substring( (280 + (Yml_2["Contents"] as String).split("\",\"\\n\",{\"font\":\"uniform\",\"text\":\"")[1].length + 33 ),(Yml_2["Contents"] as String).length ))
                        Yml_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Chat/Global_Chat_REC.yml"))
                    }
                    setYmlValue("$main_dir/Chat/Global_Chat_REC.yml","Contents",Yml_2["Contents"] as String + "\",\"\\n\",{\"font\":\"uniform\",\"text\":\"\\u00a7l" + e.player.name + "\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/interactiongui " + Player_UUID + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Interact with " + e.player.name + "\",\"color\":\"dark_green\"}]}},\"\",{\"text\":\" \\u00a73\\u00a7l➤\",\"color\":\"white\",\"font\":\"uniform\"},{\"text\":\"" + FINAL_STRING + "\",\"font\":\"uniform\",\"color\":\"white\"},\"")

                }
                if (Yml_2["Contents"] == null) setYmlValue("$main_dir/Chat/Global_Chat_REC.yml","Contents","\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n" + "\",\"\\n\",{\"font\":\"uniform\",\"text\":\"\\u00a7l" + e.player.name + "\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/interactiongui " + Player_UUID + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Chat with " + e.player.name + "\",\"color\":\"dark_green\"}]}},\"\",{\"text\":\" \\u00a73\\u00a7l➤\",\"color\":\"white\",\"font\":\"uniform\"},{\"text\":\"" + FINAL_STRING + "\",\"font\":\"uniform\",\"color\":\"white\"},\"")


                for (k in 0..Bukkit.getOnlinePlayers().size - 1){
                    var Player_DATA_COM_X = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/" + Bukkit.getOnlinePlayers().toList()[k].uniqueId + "/Common.yml"))
                    var Player_DATA_CH_X = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/" + Bukkit.getOnlinePlayers().toList()[k].uniqueId + "/Channel_And_Message.yml"))

                    if (Player_DATA_COM_X["IsOnline"] == "True"){

                        CURRENT_RECIPIENT_NAME = Player_DATA_COM_X["UserName"] as String
                        CURRENT_RECIPIENT_UUID = Bukkit.getOnlinePlayers().toList()[k].uniqueId.toString()

                        ////
                        if (Player_DATA_CH_X["Channel.CurrentChannel"] != "0" && Player_DATA_CH_X["Channel.Setting.Channel_0_Notify"] == "Enable"){

                            ADD_PLAYER_TAGS(CURRENT_RECIPIENT_UUID,"UNREAD_CHANNEL_0")
                        }
                        ////

                        if (!RECIPIENT_TAGS.contains("Disable_Channel")){
                            if (Player_DATA_CH_X["Channel.CurrentChannel"] == "0" || Player_DATA_CH_X["Channel.Setting.Channel_0_Notify"] == "Enable"){
                                CHAT_BOX_UPDATE(CURRENT_RECIPIENT_UUID)
                            }
                        }

                    }
                }
            }
            if (Player_DATA_CH["Channel.CurrentChannel"] == "1"){
                Yml_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Config.yml"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"execute as " + e.player.name + " at @s run tag @a[distance=.." + Yml_2["Chat.Region_Receiving_Range"] + "] add RECIVE_REG_CHAT")

                var nearbyplayers = Player_In_Event!!.getNearbyEntities(Player_In_Event!!.location.x,Player_In_Event!!.location.y,Player_In_Event!!.location.z).filter { it is Player } as ArrayList<Player>
                nearbyplayers.add(Player_In_Event!!)

                for (k in 0 until nearbyplayers.size ){
                    CURRENT_RECIPIENT_NAME = nearbyplayers[k].name
                    CURRENT_RECIPIENT_UUID = nearbyplayers[k].uniqueId.toString()
                    var Player_DATA_CH_REG = YamlConfiguration.loadConfiguration(File("$main_dir/Chat/RegionChat/$CURRENT_RECIPIENT_UUID.yml"))

                    if (Player_DATA_CH_REG["RegionChat.Contents"] != null){
                        if ((Player_DATA_CH_REG["RegionChat.Contents"] as String).split("\\n").size >= 240){
                            setYmlValue("$main_dir/Chat/RegionChat/$CURRENT_RECIPIENT_UUID.yml","RegionChat.Contents",(Player_DATA_CH_REG["RegionChat.Contents"] as String).substring(0,280) + (Player_DATA_CH_REG["RegionChat.Contents"] as String).substring( (280 + (Player_DATA_CH_REG["RegionChat.Contents"] as String).split("\",\"\\n\",{\"font\":\"uniform\",\"text\":\"")[1].length + 33 ),(Player_DATA_CH_REG["RegionChat.Contents"] as String).length ))
                            Player_DATA_CH_REG = YamlConfiguration.loadConfiguration(File("$main_dir/Chat/RegionChat/$CURRENT_RECIPIENT_UUID.yml"))
                        }
                        setYmlValue("$main_dir/Chat/RegionChat/$CURRENT_RECIPIENT_UUID.yml","RegionChat.Contents",Player_DATA_CH_REG["RegionChat.Contents"] as String + "\",\"\\n\",{\"font\":\"uniform\",\"text\":\"\\u00a7l" + e.player.name + "\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/interactiongui " + Player_UUID + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Interact with " + e.player.name + "\",\"color\":\"dark_green\"}]}},\"\",{\"text\":\" \\u00a73\\u00a7l➤\",\"color\":\"white\",\"font\":\"uniform\"},{\"text\":\"" + FINAL_STRING + "\",\"font\":\"uniform\",\"color\":\"white\"},\"")

                    }
                    if (Player_DATA_CH_REG["RegionChat.Contents"] == null) setYmlValue("$main_dir/Chat/RegionChat/$CURRENT_RECIPIENT_UUID.yml","RegionChat.Contents","\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n" + "\",\"\\n\",{\"font\":\"uniform\",\"text\":\"\\u00a7l" + e.player.name + "\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/interactiongui " + Player_UUID + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Chat with " + e.player.name + "\",\"color\":\"dark_green\"}]}},\"\",{\"text\":\" \\u00a73\\u00a7l➤\",\"color\":\"white\",\"font\":\"uniform\"},{\"text\":\"" + FINAL_STRING + "\",\"font\":\"uniform\",\"color\":\"white\"},\"")

                    //var Player_DATA_CHA = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Channel_And_Message.yml"))
                    ////
                    var Player_DATA_COM_CURRENT_REC = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Common.yml"))
                    var Player_DATA_CH_CURRENT_REC = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$CURRENT_RECIPIENT_UUID/Channel_And_Message.yml"))

                    if (Player_DATA_CH_CURRENT_REC["Channel.CurrentChannel"] != "1" && Player_DATA_CH_CURRENT_REC["Channel.Setting.Channel_1_Notify"] == "Enable"){
                        ADD_PLAYER_TAGS(CURRENT_RECIPIENT_UUID,"UNREAD_CHANNEL_1")
                    }
                    ////
                    RECIPIENT_TAGS = ArrayList<String>()
                    if (!(Player_DATA_COM_CURRENT_REC["Tags"] as ArrayList<String>).isNullOrEmpty()) RECIPIENT_TAGS = Player_DATA_COM_CURRENT_REC["Tags"] as ArrayList<String>

                    if (!RECIPIENT_TAGS.contains("Disable_Channel")){
                        if (Player_DATA_CH_CURRENT_REC["Channel.CurrentChannel"] == "1" || Player_DATA_CH_CURRENT_REC["Channel.Setting.Channel_1_Notify"] == "Enable"){
                            CHAT_BOX_UPDATE(CURRENT_RECIPIENT_UUID)
                        }
                    }
                }
            }
            if (Player_DATA_CH["Channel.CurrentChannel"] == "2"){

                if (Player_DATA_CH["PrivateChat.CurrentContact"] != null){

                    ////>>>>>>>>>>>>>>>
                    var SIDE_A_NAME = e.player.name
                    var SIDE_A_UUID = e.player.uniqueId.toString()

                    var SIDE_B_UUID = Player_DATA_CH["PrivateChat.CurrentContact"] as String
                    var Player_DATA_COM_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$SIDE_B_UUID/Common.yml"))
                    var SIDE_B_NAME = Player_DATA_COM_2["UserName"] as String
                    var Player_DATA_CH_2 = YamlConfiguration.loadConfiguration(File("$main_dir/Player_DATA/$SIDE_B_UUID/Channel_And_Message.yml"))


                    var Contacts = ArrayList<String>()
                    if (Player_DATA_CH["PrivateChat.Contacts"] != null) Contacts = Player_DATA_CH["PrivateChat.Contacts"] as ArrayList<String>
                    ////>>>>>>>>>>>>>>>

                    var PrivateChat_File_Path = ""
                    if (File("$main_dir/Chat/PrivateChat/" + SIDE_A_UUID + "___" + SIDE_B_UUID + ".yml").exists()){
                        PrivateChat_File_Path = "$main_dir/Chat/PrivateChat/" + SIDE_A_UUID + "___" + SIDE_B_UUID + ".yml"
                    }else if(File("$main_dir/Chat/PrivateChat/" + SIDE_B_UUID + "___" + SIDE_A_UUID + ".yml").exists()){
                        PrivateChat_File_Path = "$main_dir/Chat/PrivateChat/" + SIDE_B_UUID + "___" + SIDE_A_UUID + ".yml"
                    }else{
                        File("$main_dir/Chat/PrivateChat/" + SIDE_A_UUID + "___" + SIDE_B_UUID + ".yml").createNewFile()
                        PrivateChat_File_Path = "$main_dir/Chat/PrivateChat/" + SIDE_A_UUID + "___" + SIDE_B_UUID + ".yml"
                    }

                    if (PrivateChat_File_Path != ""){
                        Yml_1 = YamlConfiguration.loadConfiguration(File(PrivateChat_File_Path))
                        if (Yml_1["PrivateChat.Contents"] != null){
                            if ((Yml_1["PrivateChat.Contents"] as String).split("\\n").size >= 240){
                                setYmlValue(PrivateChat_File_Path,"PrivateChat.Contents",(Yml_1["PrivateChat.Contents"] as String).substring(0,280) + (Yml_1["PrivateChat.Contents"] as String).substring( (280 + (Yml_1["PrivateChat.Contents"] as String).split("\",\"\\n\",{\"font\":\"uniform\",\"text\":\"")[1].length + 33 ),(Yml_1["PrivateChat.Contents"] as String).length ))
                                Yml_1 = YamlConfiguration.loadConfiguration(File(PrivateChat_File_Path))
                            }
                            setYmlValue(PrivateChat_File_Path,"PrivateChat.Contents",Yml_1["PrivateChat.Contents"] as String + "\",\"\\n\",{\"font\":\"uniform\",\"text\":\"\\u00a7r \\u00a7l" + e.player.name + "\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/interactiongui " + Player_UUID + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Interact with " + e.player.name + "\",\"color\":\"dark_green\"}]}},\"\",{\"text\":\" \\u00a73\\u00a7l➤\",\"color\":\"white\",\"font\":\"uniform\"},{\"text\":\"" + FINAL_STRING + "\",\"font\":\"uniform\",\"color\":\"white\"},\"")

                        }
                        if (Yml_1["PrivateChat.Contents"] == null) setYmlValue(PrivateChat_File_Path,"PrivateChat.Contents","\\nhahaha\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n" + "\",\"\\n\",{\"font\":\"uniform\",\"text\":\"\\u00a7r \\u00a7l" + e.player.name + "\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/interactiongui " + Player_UUID + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Chat with " + e.player.name + "\",\"color\":\"dark_green\"}]}},\"\",{\"text\":\" \\u00a73\\u00a7l➤\",\"color\":\"white\",\"font\":\"uniform\"},{\"text\":\"" + FINAL_STRING + "\",\"font\":\"uniform\",\"color\":\"white\"},\"")

                    }

                    //////
                    if (Contacts.contains("" + SIDE_B_UUID)){

                        Contacts.remove("" + SIDE_B_UUID)
                    }
                    Contacts.add(0,"" + SIDE_B_UUID)
                    setYmlValue("$main_dir/Player_DATA/$SIDE_A_UUID/Channel_And_Message.yml","PrivateChat.Contacts",Contacts)
                    //////

                    CHAT_BOX_UPDATE(SIDE_A_UUID)
                    //==================================================================================================
                    ////>>>>>>>>>>>>>>>

                    if (Player_DATA_CH_2["PrivateChat.CurrentContact"] == null) setYmlValue("$main_dir/Player_DATA/$SIDE_B_UUID/Channel_And_Message.yml","PrivateChat.CurrentContact",SIDE_A_UUID)

                    Contacts = ArrayList<String>()
                    if (Player_DATA_CH_2["PrivateChat.Contacts"] != null) Contacts = Player_DATA_CH_2["PrivateChat.Contacts"] as ArrayList<String>
                    ////>>>>>>>>>>>>>>>


                    //////
                    if (Contacts.contains("" + SIDE_A_UUID)){

                        Contacts.remove("" + SIDE_A_UUID)
                    }
                    Contacts.add(0,"" + SIDE_A_UUID)
                    setYmlValue("$main_dir/Player_DATA/$SIDE_B_UUID/Channel_And_Message.yml","PrivateChat.Contacts",Contacts)
                    //////

                    ////////
                    if (Player_DATA_CH_2["Channel.CurrentChannel"] != "2" &&  Player_DATA_CH_2["Channel.Setting.Channel_2_Notify"] == "Enable"){
                        ADD_PLAYER_TAGS(SIDE_B_UUID,"UNREAD_CHANNEL_2")
                    }
                    if (Player_DATA_CH_2["PrivateChat.CurrentContact"] != SIDE_A_UUID){
                        ADD_PLAYER_TAGS(SIDE_B_UUID,"UNREAD_CONTACT_" + SIDE_A_UUID)
                    }
                    ////////

                    if (!RECIPIENT_TAGS.contains("Disable_Channel")){
                        if (Player_DATA_CH_2["Channel.CurrentChannel"] == "2" || Player_DATA_CH_2["Channel.Setting.Channel_2_Notify"] == "Enable"){
                            CHAT_BOX_UPDATE(SIDE_B_UUID)
                        }
                    }
                }
            }
        }
    }
//######################################################################################################################
//######################################################################################################################
//CHAT_BOX_UPDATE
    fun CHAT_BOX_UPDATE(Player_UUID_AAAA: String){

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"chatbox_update " + Player_UUID_AAAA)

    }
//######################################################################################################################
//######################################################################################################################
//FOOTER_LABEL
    fun FOOTER_LABEL(){

    }
//######################################################################################################################
//######################################################################################################################
@EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDamage_XXX(e: EntityDamageEvent){

    }
}























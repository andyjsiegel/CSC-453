public class Standard {

   public static BasicType IntType;
   public static BasicType CharType;
   public static BasicType RealType;
   public static BasicType BoolType;
   public static TypeSy NoType;
   public static Symbol NoSy;

   public static SyTab TheStandardSyTab;

   public static boolean IsStandardSymbol(Symbol S) {
      return  S == NoType   || 
              S == NoSy     || S == IntType || S == CharType || S == BoolType || 
              S == RealType;
   }

   public static void init() {
      NoType     = new BasicType("$NOTYPE", 0, 0);
      NoSy       = new TempSy("$NOSYMBOL",NoType);

      IntType    = new BasicType("int", 0, 0);
      RealType   = new BasicType("flt", 0, 0);
      CharType   = new BasicType("char", 0, 0);
      BoolType   = new BasicType("bool", 0, 0);

      SyTab S = SyTab.create();
      S = S.add(IntType);
      S = S.add(RealType);
      S = S.add(CharType);

      TheStandardSyTab = S;
   }

   public static SyTab SyTab () {
      return TheStandardSyTab;
   }

   public static Env env () {
      Env e = new Env(TheStandardSyTab);
      return e;
   }

}

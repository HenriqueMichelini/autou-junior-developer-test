type HeaderProps = {
  title: string;
  subtitle: string;
};

function Header({ title, subtitle }: HeaderProps) {
  return (
    <div className="flex flex-col bg-surface h-50">
      <p className="text-[6rem] text-text-default font-semibold">{title}</p>
      <p className="text-[3rem] text-text-default font-semibold">{subtitle}</p>
    </div>
  );
}

export default Header;

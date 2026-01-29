type HeaderProps = {
  title: string;
  subtitle: string;
};

function Header({ title, subtitle }: HeaderProps) {
  return (
    <div className="flex bg-surface">
      <p className="text-8xl text-text-default">{title}</p>
      <p className="text-7xl text-text-default">{subtitle}</p>
    </div>
  );
}

export default Header;

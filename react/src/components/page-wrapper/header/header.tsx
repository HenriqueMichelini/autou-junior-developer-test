type HeaderProps = {
  title: string;
  subtitle: string;
};

function Header({ title, subtitle }: HeaderProps) {
  return (
    <div className="flex flex-col h-50 rounded-sm">
      <p className="text-[5rem] text-text-default align-text-bottom font-semibold">
        {title}
      </p>
      <p className="text-[2rem] text-text-default align-text-bottom font-semibold">
        {subtitle}
      </p>
    </div>
  );
}

export default Header;

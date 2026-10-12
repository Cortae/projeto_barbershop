export default function Avatar({ name = '', ...props }) {
  const initials = name.split(' ').map((p) => p[0]).slice(0, 2).join('').toUpperCase()
  return (
    <button
      type="button"
      className="flex size-11 items-center justify-center rounded-full bg-secundaria font-semibold text-primaria-hover"
      {...props}
    >
      {initials}
    </button>
  )
}
